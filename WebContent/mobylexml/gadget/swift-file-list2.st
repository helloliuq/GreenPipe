$typedeflist: { typedef |
 type $typedef$
}$
($outArgs: { it | $it.type$ $it.name$};  separator="," $) run$application$($inArgs: { it | $it.type$ $it.name$};  separator="," $){
   $application$ $CmdArgs: { it | $it$ }$
}
$fileMappings:{ arg | $arg$ }$
  
$inoutArgs:{ arg |
  $if(arg.isloop)$
    $arg.type$ $arg.name$s [] <fixed_array_mapper;files="$arg.value$">;
  $else$
    $arg.type$ $arg.name$ <single_file_mapper;file="$arg.value$">;
  $endif$
  
}$

$if(Foreach)$
foreach(  $LoopArg.name$, i in $LoopArg.name$s ) {
  $outArgs: { it | $it.name$s[i]};  separator="," $=run$application$($inArgs: { it | $if(it.isloop)$$it.name$s[i]$else$$it.name$$endif$ };separator="," $);
 }
}
$else$
$outArgs: { it | $it.name$};  separator="," $=run$application$($inArgs: { it | $it.name$}; separator="," $);
$endif$

# create a temp list for the selected files (inputLinks?)

# upload multiple files together