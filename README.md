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

If `heartbeats-simple` is not installed to a default location, you need to set the `PKG_CONFIG_PATH` environment variable or export it to your environment so that `pkg-config` can discover the library.
Unless you are skipping tests (`-DskipTests=true`), you must do the same for `LD_LIBRARY_PATH`.

```sh
PKG_CONFIG_PATH=/path/to/heartbeats-simple/install/lib/pkgconfig:$PKG_CONFIG_PATH \
  LD_LIBRARY_PATH=/path/to/heartbeats-simple/install/lib/:$LD_LIBRARY_PATH \
  mvn clean package
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
