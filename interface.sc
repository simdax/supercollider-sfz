SFZProxy{

	var <sfz;
	classvar root;

	famille{
		^[\brass,
			\chorus,
			\keys, \percussion, \strings, \woodwinds
		]
	}
	
	gui{
		// TODO add filter + root box
		EZListView().items_(
			PathName(
				"/home/simdax/Téléchargements/sfz/sonatina/"
			).entries
			.select{arg x;
				x.extension=="sfz"}
			.collect{arg x;
				x.fileName
				-> {this.load(x.absolutePath)}
			}
		)
		
	}
	
	*initClass{
		root="/home/simdax/Téléchargements";
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
	load{ arg path;
		Server.default.waitForBoot
		{
			sfz !? {sfz.free};
			sfz=SFZ(path);
			sfz.prepare;
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