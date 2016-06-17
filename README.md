# Heartbeats-Simple Java Bindings

This project provides Java bindings and thin wrappers around `heartbeats-simple`.

## Dependencies

The `heartbeats-simple` libraries and headers should be installed to the system.

The latest `heartbeats-simple` source can be found at
[https://github.com/libheartbeats/heartbeats-simple](https://github.com/libheartbeats/heartbeats-simple).

## Building

This project uses [Maven](http://maven.apache.org/).
Currently the only supported platforms are the `unix` family.
To build and run junit tests:

```sh
mvn clean install
```

You may need to add the JDK's native include path by setting the `CFLAGS` property as part of the build command, e.g.:
```sh
mvn clean package -DCFLAGS=-I$JAVA_HOME/include/linux
```

If `heartbeats-simple` is compiled but not installed, you may need to set the `CFLAGS` and `LDFLAGS` properties.
Unless you are skipping tests (`-DskipTests`), you also need to set the `LD_LIBRARY_PATH` environment variable or export it to your environment.

```sh
LD_LIBRARY_PATH=/path/to/heartbeats-simple/_build:$LD_LIBRARY_PATH \
  mvn clean package \
  -DCFLAGS="-I$JAVA_HOME/include/linux -I/path/to/heartbeats-simple/inc" \
  -DLDFLAGS=-L/path/to/heartbeats-simple/_build
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

* `edu.uchicago.cs.heartbeats.Heartbeat`:`edu.uchicago.cs.heartbeats.DefaultHeartbeat`
* `edu.uchicago.cs.heartbeats.HeartbeatAccuracy`:`edu.uchicago.cs.heartbeats.DefaultHeartbeatAccuracy`
* `edu.uchicago.cs.heartbeats.HeartbeatPower`:`edu.uchicago.cs.heartbeats.DefaultHeartbeatPower`
* `edu.uchicago.cs.heartbeats.HeartbeatAccuracyPower`:`edu.uchicago.cs.heartbeats.DefaultHeartbeatAccuracyPower`

When launching, you will need to set the property `java.library.path` to include the location of a native library created by this project: `libheartbeats-simple-wrapper`.
