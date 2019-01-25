Changelog
=========

2.0.0 (2019-01-??)
-------------------
- [MAJOR] Re-branded all projects from NWTSJavaCode to OddSource Code
- [MAJOR] The base package for all classes and packages in this project has been renamed from
  ``net.nicholaswilliams.java.licensing`` to ``io.oddsource.java.licensing``. Except as otherwise noted in other
  "[MAJOR]" changes below, all the classes remain interface compatible with 1.x, so you should need only to perform a
  bulk replace in your project.
- [MAJOR] ``ImmutableModifiedThroughReflectionException`` has been renamed to
  ``ImmutableModifiedThroughReflectionError`` to follow proper Java exception naming procedures.
- [MAJOR] ``InsecureEnvironmentException`` has been renamed to ``InsecureEnvironmentError`` to follow proper Java
  exception naming procedures.
- [MAJOR] The following methods in ``License.Builder``, previously marked as deprecated in 1.x, have now been removed:

  - ``withFeature(String featureName)``: Use ``addFeature(String featureName)``, instead.
  - ``withFeature(String featureName, long goodBeforeDate)``: Use
    ``addFeature(String featureName, long goodBeforeDate)``, instead.
  - ``withFeature(License.Feature feature)``: Use ``addFeature(License.Feature feature)``, instead.

- [MAJOR] The properties for the private key properties file and license-generate properties file used by the CLI have
  changed. The properties were previously prefixed with ``net.nicholaswilliams.java.licensing``. They are now prefixed
  with ``io.oddsource.java.licensing``.
- [MAJOR] Switched to using Travis CI for all continuous integration
- [MAJOR] Switched to the ``nexus-staging-maven-plugin`` for deployments instead of ``maven-deploy-plugin``
- [MAJOR] Switched from tabs to spaces
- [MINOR] Codified explicit code style rules
- [MINOR] Configured mandatory license header check in Maven build
- [MINOR] Fixed #3: License Manager should now run under Java 10
- [MINOR] Complete code reformat

1.1.0 (2013-04-25)
------------------
- [MINOR] A few simple bug fixes in the POM

1.0.0 (2011-05-01)
------------------
- [MAJOR] Initial release
