A work-in-progress sample player for the SFZ format written in SuperCollider. Currently, it implements some of SFZ v1, containing enough critical features to handle a good number of simple sample packs.

I am developing this vertically, implementing features only as I need them. If you're using this software and you need something that isn't currently supported, please file an issue. If you need something more stable, try [LinuxSampler](http://www.linuxsampler.org/).

    x = SFZ("/path/to/sfz/file.sfz");
    // load buffers
    x.prepare { "done".postln };

    x.noteOn(64, 60);
    x.noteOff(64, 60);



SFZProxy
==

I added that to easify the manipulation. Check examples, io.
