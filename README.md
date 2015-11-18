# Heartbeats-Simple Java Bindings

This project provides Java bindings and thin wrappers around the `heartbeats-simple` libraries.

## Dependencies

The `heartbeats-simple` libraries and headers should be installed to the system.

The latest `heartbeats-simple` C libraries can be found at
[https://github.com/libheartbeats/heartbeats-simple](https://github.com/libheartbeats/heartbeats-simple).

## Building

This project uses `Maven`.
Currently the only supported platforms are the `unix` family.)
To build and run junit tests:

```sh
mvn clean install
```

If the `heartbeats-simple` libraries are compiled but not installed, you need to specify the `CFLAGS` and `LDFLAGS` properties as part of the build command.
Unless you are skipping tests (`-DskipTests`), you also need to set the `LD_LIBRARY_PATH` environment variable or export it to your environment.

```sh
LD_LIBRARY_PATH=/path/to/heartbeats-simple/_build/lib:$LD_LIBRARY_PATH \
  mvn clean package \
  -DCFLAGS=-I/path/to/heartbeats-simple/inc -DLDFLAGS=/path/to/heartbeats-simple/_build/lib
```

## Usage

To integrate with the library, add it as a Maven dependency to your project's `pom.xml`:

```xml
    <dependency>
      <groupId>edu.uchicago.cs.heartbeats</groupId>
      <artifactId>heartbeats-simple</artifactId>
      <version>0.0.1-SNAPSHOT</version>
    </dependency>
```

The following `interface`:`implementation` pairs are available:

* `edu.uchicago.cs.heartbeats.Heartbeat`:`edu.uchicago.cs.heartbeats.DefaultHeartbeatJNI`
* `edu.uchicago.cs.heartbeats.HeartbeatAccuracy`:`edu.uchicago.cs.heartbeats.DefaultHeartbeatAccuracyJNI`
* `edu.uchicago.cs.heartbeats.HeartbeatPower`:`edu.uchicago.cs.heartbeats.DefaultHeartbeatPowerJNI`
* `edu.uchicago.cs.heartbeats.HeartbeatAccuracyPower`:`edu.uchicago.cs.heartbeats.DefaultHeartbeatAccuracyPowerJNI`

Note that the default implementations are not thread safe - you must provide synchronization as needed!

When launching, you will need to set the property `java.library.path` to include the location of the native libraries created by the modules `libhbs-wrapper`, `libhbs-acc-wrapper`, `libhbs-pow-wrapper`, and `libhbs-acc-pow-wrapper`.
