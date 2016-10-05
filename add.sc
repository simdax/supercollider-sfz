+ SFZ {
	fileName{
		^PathName(sfzPath).fileName
	}
	printOn{arg stream;
		stream << this.fileName
	}
}