# Pandamation

Pandamation is an open source android framework for frame animation.  The built in android framework has a few issues that
this library solves.  The first of these issues are the memory issues with android's.  When a frame animation is done with
androids framework it loads all of the frames in to memory up front.  This causes out of memory exceptions if you use large
frames or a large number of frames.  Additionally android's framework does not provide a method to provide a frame number
to stop on.  This functionality allows you to use a frame animation for progress or other of those types of tasks and 
allows you to use it for intermediary values.  The goal was to use the same animation list xml that the android version uses.


# Setup

Build.Gradle

- Add Maven
- ```
- maven {
            url "https://oss.sonatype.org/content/repositories/snapshots"
        }
```

- Add the project
```
compile"com.github.jaredcorso.Pandamation:library:1.1-SNAPSHOT"
```

additionally the project requires apache's IO Utils so you will also need to add
```
compile 'org.apache.directory.studio:org.apache.commons.io:2.4'
```


#Using the Framework

There are two main usages that can be used statically as shown below:

```
Pandamate.animate(int resourceId, final ImageView imageView, final Runnable onStart, final Runnable onComplete)
Pandamate.animateWithStopFrame(int resourceId, final ImageView imageView, final Runnable onStart, final Runnable onComplete, int frameToEndOn)
```

- resourceId : the id to the xml with the animation list (identical to the format the android version requires)
- imageView : the imageview you want the animation to be loaded into
- onStart : a runable you want run before the animation
- onComplete : a runable you want run after the animation is complete
- frameToStopOn : (2nd method only) the frame number you want the animation to stop on

#Footnote
special props to Ryan Baumbach for coming up with the name for this library
