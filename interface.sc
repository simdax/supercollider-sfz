SFZProxy{

	var <sfz,
	// when loading buffers while playing
	// g is GUI Window
	<g;
	classvar go=false;

	classvar <>root="/home/simdax/Téléchargements/sfz/sonatina/";
	classvar <all;
	*clearAll{
		all.do(_.free)
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
			go.if
			{
				fork{
					~inst.noteOn(amp, note);
					(~dur*~legato).wait;
					~inst.noteOff(amp, note);
					(~dur*(1-~legato)).wait;
				}
			}
			?? { ~type=\rest}
		});	
	}

	// root searching etc..
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
	
	// GUI thing
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
	*gui{ arg p=FlowView(),b;
		all.do { |item, index|
			item.gui(p,label:index)
		};
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
			go=false;
			sfz=SFZ(PathName(path).absolutePath);
			sfz.prepare({go=true});
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
		sfz ?? {Error("vazi, charge du sfz d'abord").throw};
		^(Pbind(\type, \sfz,\inst, Pfunc{sfz})
			<>
			Pbind(*args)
			<> (amp:0.8) // or you won't hear ...
		)
	}
}