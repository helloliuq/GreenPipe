$typedeflist: { typedef |
 type $typedef$
}$
($outArgs: { it | $it.type$ $it.name$};  separator="," $) run$application$($inArgs: { it | $it.type$ $it.name$};  separator="," $){
   $application$ $CmdArgs: { it | $it$ }$
}
$inoutArgs:{ arg |
  $arg.type$ $arg.name$s [] <fixed_array_mapper;files="$arg.value$">;
}$

$inArgs: { arg |
 foreach(  $arg.name$, i in $arg.name$s ) {
  $outArgs: { it | $it.name$s[i]};  separator="," $=run$application$($inArgs: { it | $it.name$s[i]}; separator="," $);
 }
}$

# create a temp list for the selected files (inputLinks?)

# upload multiple files together