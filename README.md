java_esper_epl_helper
=====================

Java demo application to help understand and work better with Esper EPL


This code is organized as an Eclipse project. It offers two main components:

1) A very basic example of setting up Esper to process EPL queries. The examples that come with Esper were a bit too comprehensive for me. There didn't seem to be a simple plain Hello World example. All the examples are in the context of a real world scenario, like stock ticker or shell commands. If you look at the event.score package, you will see a simple example. The main class that runs the Event Score example is in the .demo package.

2) A way to run EPL queries through a config file. This includes configuring which listener class will be associated with which specific SELECT query.
