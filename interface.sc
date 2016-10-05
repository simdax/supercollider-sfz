SFZProxy{

	var <sfz,
	// g is GUI Window
	<g;

	classvar <>root="/home/simdax/Téléchargements/sfz/sonatina/";
	classvar <all;
	*clearAll{
		all.do{_.free}
	}
	*new{
		var res=super.new;
		all.add(res)
		^res
	}
	*initClass{
		Class.initClassTree(ObjectTable);
		all=ObjectTable();
		Event.addEventType(\sfz, {
			var amp=\midivelocity.asSpec.map(~amp.value); 
			var note=~midinote.value;
			~inst !?
			{r{
				~inst.noteOn(amp, note);
				(~dur*~legato).wait;
				~inst.noteOff(amp, note)
			}.play	}
			?? { ~type=\rest}
		});	
	}
	*entries{
		^PathName(
			this.root
		).entries.select{arg x;	x.extension=="sfz"}
	}
	*famille{
		^[\brass,
			\chorus,
			\keys, \percussion, \strings, \woodwinds
		]
	}
	*find{ arg fam=this.famille.choose;
		^this.entries
		.select{arg x;(fam.asString).matchRegexp(x.fileName)}
		.collect(_.absolutePath)
	}
	gui{ arg p, b, label;
		// TODO add filter + root box
		g=EZListView(p,b,label).items_(
			this.class.entries
			.collect{arg x;
				x.fileName
				-> {this.load(x.absolutePath)}
			}
		)
		
	}
	findIndex{
		arg name;
		^g.items.detectIndex{arg assoc;
			name==assoc.key
		}
	}
	load{ arg path;
		Server.default.waitForBoot
		{
			sfz !? {sfz.free};
			sfz=SFZ(PathName(path).absolutePath);
			sfz.prepare;
			g!?{g.value_(
				this.findIndex(PathName(path).fileName)
			)}
		}
	}
	openPanel{
		var f;
		fork{
			Server.default.waitForBoot;
			f=FlowVar();
			defer{Dialog.openPanel{arg x; f.value_(x)}};
			f.value;
			this.load(f.value);
		}
	}
	// pattern{ arg ... args;
	// 	sfz !?{
	// 		^
	// 		Pdef(\_sfz,
	// 		// Pfset(
	// 		// 	{ var ok=false;
	// 		// 		~inst=sfz.prepare({ok=true});
	// 		// 		while{ok.not}{0.1.wait};
	// 		// 	},
	// 			Pbind(
	// 				*[\inst, sfz, \type, \sfz]++args
	// 			),
	// 		//				{sfz.free}
	// 		//)
	// 	)
	// 	}
	// 	?? {
	// 		Error("pas de sfz chargé!".throw)
	// 	}
	// }

	pattern{ arg ... args;
		^(Pbind(\type, \sfz, \inst, Pfunc{sfz})<>Pbind(*args))
	}
}