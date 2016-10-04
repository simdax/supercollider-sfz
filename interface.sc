SFZProxy{

	var <sfz;
	classvar root;
	
	*initClass{
		root="/home/simdax/Téléchargements";
		Event.addEventType(\sfz, {
			var amp=\midivelocity.asSpec.map(~amp.value)*2; 
			var note=~midinote.value;
			//	if(amp<1){amp=60};
			r{
				~inst.noteOn(amp, note);
				(~dur*~legato).wait;
				~inst.noteOff(amp, note)
			}.play	
		});	
	}
	openPanel{
		var f;
		fork{
			Server.default.waitForBoot;
			f=FlowVar();
			defer{Dialog.openPanel{arg x; f.value_(x)}};
			f.value;
			sfz=SFZ(f.value);
			sfz.prepare
		}
	}
	pattern{ arg ... args;
		sfz !?{
	 	^Pdef(\_sfz,
			// Pfset(
			// 	{ var ok=false;
			// 		~inst=sfz.prepare({ok=true});
			// 		while{ok.not}{0.1.wait};
			// 	},
				Pbind(
					*[\inst, sfz, \type, \sfz]++args
				),
			//				{sfz.free}
			//)
		)
		}
		?? {
			Error("pas de sfz chargé!".throw)
		}
	}
}