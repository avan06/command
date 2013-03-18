package com.dummi.common.command;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.SparseArray;

/**
 * This class is the utility for UIHandler
 * Construction, argument assignment and some other works
 * @author trinity
 *
 */
public class Command {

	@SuppressWarnings("unchecked")
	protected static <T> T $(Object o) {
		return (T) o;
	}

	// obj - Runnable
	public static final int RUN = 0;
	
	private static final SparseArray<Method> exec = new SparseArray<Method>();
	
	public static void register(Class<?> clazz) {
		Method[] methods = clazz.getDeclaredMethods();
		for(Method m: methods) {
			int mod = m.getModifiers();
			if(Modifier.isPublic(mod)){
				Resolver annot = m.getAnnotation(Resolver.class);
				if(annot == null) continue;
				int id = annot.id();
				Method m1 = exec.get(id);
				if(m1 != null)
					throw new ResolverDelegateIdentifierException(id, m1, m);
				exec.append(id, m);
			}
		}
	}
	
	private static Map<Looper, Handler> _Handlers = new HashMap<Looper, Handler>();
	
	private static class _Handler extends Handler {
		
		public _Handler(Looper looper) {
			super(looper);
		}

		public void handleMessage(Message msg) {
			switch(msg.what){
			case RUN:
				if(msg.obj instanceof Runnable) {
					Runnable runnable = $(msg.obj);
					runnable.run();
				}
				break;
			default:
				Method m = exec.get(msg.what);
				if(m == null) 
					throw new ResolverNotFoundException(msg.what);
				Object[] args = $(msg.obj);
				try {
					if(Modifier.isStatic(m.getModifiers())) {
						m.invoke(null, args);
					}else{
						m.invoke(args[0], Arrays.asList(args).subList(1, args.length));
					}
				} catch (Exception e) {
					throw new ResolverInvokeException(msg.what, e);
				}
			}
		}

	};
	
	private static Handler obtainHandler(Looper looper) {
		Handler h = _Handlers.get(looper);
		if(h == null) {
			_Handlers.put(looper, new _Handler(looper));
			return obtainHandler(looper);
		}
		return h;
	}
	
	public static Command invoke(Runnable run) {
		Command cmd = new Command();
		cmd.msg.what = RUN;
		cmd.msg.obj = run;
		return cmd;
	}

	public static Command invoke(int progId) {
		Command cmd = new Command();
		cmd.msg.what = progId;
		return cmd;
	}

	public static void revoke(int progId) {
		for(Handler h : _Handlers.values())
			h.removeMessages(progId);
	}

	private final Message msg;
	
	private Handler Commander;

	private Command() {
		Looper l = Looper.myLooper();
		if(l == null) l = Looper.getMainLooper();
		Commander = obtainHandler(l);
		msg = Message.obtain();
	}
	
	public Command runAt(Looper looper) {
		Commander = obtainHandler(looper);
		return this;
	}
	
	public Command to(Looper looper) {
		return runAt(looper);
	}
	
	public Command at(Looper looper) {
		return runAt(looper);
	}
	
	public Command atMain() {
		return runAt(Looper.getMainLooper());
	}
	
	public Command atCurrent() {
		return runAt(Looper.myLooper());
	}

	public Command args(Object... args) {
		msg.obj = args;
		return this;
	}
	
	public Command only() {
		Command.revoke(msg.what);
		return this;
	}
	
	public Command exclude(int progId) {
		Command.revoke(progId);
		return this;
	}

	public Message getMessage() {
		return msg;
	}

	public void send() {
		Commander.sendMessage(msg);
	}

	public void sendAtTime(long timeMillis) {
		Commander.sendMessageAtTime(msg, timeMillis);
	}

	public void sendDelayed(long delay) {
		Commander.sendMessageDelayed(msg, delay);
	}

}
