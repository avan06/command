Command
=======

A Java utility library for handling asynchronize events.

Command utility is a wrapper to `Handler` of Android SDK. Providing a
relatively clean and easy access to Handler API using chaining call.

Usage
-----

There is two part using Command. One is `Command`, the other is `Resolver`.

`Command` is used to **issue** specific event with some arguments. And
`Resolver` is created to **handle** the raised events.

Here is a Resolver declaration code 

```Java

  public static final int SCROLL_LIST_FOR_DISTANCE_IN_ANY_MILLIS = 10;
  
  @Resolver(id = SCROLL_LIST_FOR_DISTANCE_IN_ANY_MILLIS)
  public static void SCROLL_LIST_FOR_DISTANCE_IN_ANY_MILLIS(ListView v, int distance, int duration) {
    v.smoothScrollBy(distance, duration);
  }

```

We create a resolver function and assign an id of `10`. When the
`SCROLL_LIST_FOR_DISTANCE_IN_ANY_MILLIS` event is fired, function is invoked
in asynchronized manner. The resolver scrolls a view to the distance.

In addition, you will need to register your class to Command. You can add
the following code to everywhere in your class. To the previous example, I
put the Resolver in class named `U`.

```Java

  static {
    // Register Async Operation
    com.dummi.common.command.Command.register(U.class);
  }

```

Then, we will be able to fire the event.

```Java

  Command.invoke(SCROLL_LIST_FOR_DISTANCE_IN_ANY_MILLIS) // Fire SCROLL_LIST_FOR_DISTANCE_IN_ANY_MILLIS
         .args(parent, viewY, 300)                       // Pass arguments
         .only()                                         // Remove other Fire request to the same Event
         .sendDelayed(100);                              // Fire the event after 100ms

```

License
-------

Copyright 2013 Dummi Studio&lt;oss@dummi.org&gt;

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and 
limitations under the License.

