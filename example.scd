(
f=SFZProxy();
// choose sfz file
//f.openPanel;
// or
f.gui; // take root file var
)

(//play it
b=f.pattern(\amp, 0.8, \dur, 2, \degree, Pseries()%7);
b.trace.play;

)

