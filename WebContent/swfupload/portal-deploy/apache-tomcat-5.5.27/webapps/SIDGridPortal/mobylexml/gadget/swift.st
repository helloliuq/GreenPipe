$typedeflist: { typedef |
 type $typedef$;
}$
$if(outArgs)$($outArgs: { it | $it.type$ $it.name$};  separator="," $)$endif$ run$application$($inArgs: { it | $it.type$ $it.name$};  separator="," $){
  app {
   $command$ $CmdArgs: { it | $it$ }$;
  }
}
$inoutArgs:{ arg |
  $arg.type$ $arg.name$ <single_file_mapper;file="$arg.value$">;
}$
$outArgs: { it | $it.name$}; separator="," $ $if(outArgs)$=$endif$run$application$($inArgs: { it | $it.name$}; separator="," $);
