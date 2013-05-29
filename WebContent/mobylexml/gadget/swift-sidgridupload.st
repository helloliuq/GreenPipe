runimport(string expid, string expName, File infile){
  app {
   importresult expid expName @infile;
  }
}

$inoutArgs:{ arg |
  $arg.type$ $arg.name$ <single_file_mapper;file="$arg.value$">;
}$

runimport("$expid$", "$expName$", $outArgs: { it | $it.name$};  separator="," $);
