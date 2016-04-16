# Introduction #

Great effort is made to fully document the server's API. The documentation is written in the Javadoc format to allow for nicely formatted API documentation readable in the browser.

This page contains instructions on building the Javadoc documentation from the code, as well as a link to prebuilt (and hopefully fairly up-to-date) documentation.

# Building from code #

Building the Javadoc from a fresh Subversion checkout of the code is the best way to guarantee you have the most up-to-date version of the documentation there is. Although I try to keep the prebuilt version linked below as current as possible, it may not be the most bleeding edge. If you want to develop for the server, a fresh copy is best.

You can build the Javadoc either through the Eclipse IDE, or from the command line with the `javadoc` tool.

## Using the Eclipse IDE ##

Checkout the code from the Subversion repository using either the command-line tool, a [graphical client](http://tortoisesvn.tigris.org/), or the [Eclipse plugin](http://subclipse.tigris.org/). Import the project into your workspace. Then go to **Project--Generate Javadoc**. Ensure that the entire `src/` directory of the InternetShogiServer project is checked. Choose the visibility level that you want. Hit **Finish** when the settings are to your liking. The Javadoc will be generated and the console output will tell you where to find it.

# Prebuilt #

You can find the latest compilation [here](http://komrades.org/shogiserver/javadoc/).