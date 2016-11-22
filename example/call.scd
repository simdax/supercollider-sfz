(
a={SFZProxy()} ! 3;
a.do(_.load(SFZProxy.entries[30.rand].absolutePath));
//a.do(_.gui);
SFZProxy.gui
)

(
b=Pbind(\degree, [0,1,2,3].pseq(inf));
c=Ppar(
	a.collect(_.pattern)
	.collect({|item,index| item <> Pbindf(b, \mtranspose, index*2)
	})
);
c.trace.play

)

