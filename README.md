# android-protocol-buffers

## Introduction

This PoC (*Proof Of Concept*) uses Protocol Buffers exchange data between an Android app and a backend.

It also uses both *SQLite* and [*Realm*](https://realm.io) for persist large amounts of data.


## SQLite Issue

There is an issue about size of records on SQLite on Android.
In my case, I'm saving an image on database that is larger than 2MB.

It is related with this setting in Android framework: [config_cursorWindowSize](http://stackoverflow.com/questions/21432556/android-java-lang-illegalstateexception-couldnt-read-row-0-col-0-from-cursorw)

When I try to read a record from the database, i got this:

```
05-17 16:44:23.982 18128-18128/com.josenaves.androidprotocolbuffers W/CursorWindow: Window is full: requested allocation 3203596 bytes, free space 2096659 bytes, window size 2097152 bytes
05-17 16:44:23.982 18128-18128/com.josenaves.androidprotocolbuffers E/CursorWindow: Failed to read row 0, column 0 from a CursorWindow which has 0 rows, 4 columns.
05-17 16:44:23.982 18128-18128/com.josenaves.androidprotocolbuffers D/AndroidRuntime: Shutting down VM
05-17 16:44:23.982 18128-18128/com.josenaves.androidprotocolbuffers W/dalvikvm: threadid=1: thread exiting with uncaught exception (group=0x41c27e48)
05-17 16:44:23.992 18128-18128/com.josenaves.androidprotocolbuffers E/AndroidRuntime: FATAL EXCEPTION: main
                                                                                      Process: com.josenaves.androidprotocolbuffers, PID: 18128
                                                                                      java.lang.IllegalStateException: Couldn't read row 0, col 0 from CursorWindow.  Make sure the Cursor is initialized correctly before accessing data from it.
                                                                                          at android.database.CursorWindow.nativeGetLong(Native Method)
                                                                                          at android.database.CursorWindow.getLong(CursorWindow.java:507)
                                                                                          at android.database.AbstractWindowedCursor.getLong(AbstractWindowedCursor.java:75)
                                                                                          at android.database.AbstractCursor.moveToPosition(AbstractCursor.java:220)
                                                                                          at android.database.AbstractCursor.moveToFirst(AbstractCursor.java:237)
                                                                                          at com.josenaves.androidprotocolbuffers.data.sqlite.ImagesDataSource.getAllImages(ImagesDataSource.java:54)
                                                                                          at com.josenaves.androidprotocolbuffers.MainActivity$1$1$1.run(MainActivity.java:81)
                                                                                          at android.os.Handler.handleCallback(Handler.java:733)
                                                                                          at android.os.Handler.dispatchMessage(Handler.java:95)
                                                                                          at android.os.Looper.loop(Looper.java:136)
                                                                                          at android.app.ActivityThread.main(ActivityThread.java:5103)
                                                                                          at java.lang.reflect.Method.invokeNative(Native Method)
                                                                                          at java.lang.reflect.Method.invoke(Method.java:515)
                                                                                          at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:790)
                                                                                          at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:606)
                                                                                          at dalvik.system.NativeStart.main(Native Method)
```

#
