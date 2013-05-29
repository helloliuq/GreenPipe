
var valueOnlyArgument = 0
var switchArgument = 1
var dashEqualsArgument = 2
var dashSpaceArgument = 3
var dashDashEqualsArgument = 4
var dashDashSpaceArgument = 5
var dashDashSwitchArgument = 6

function parseCommandLineArgs(commandLine){
  var ret = {};
  var args = {};
  var params = [];

  arguments = commandLine.split(' ');
  ret['cmd'] = arguments.shift();

  for( var i in arguments )
  {
    var arg = arguments[i];
    var opt = arg;
    var value = "";
    if( arg.indexOf( "-" ) == 0 )
    {
      // -opt=value
      if( arg.indexOf('=') > 0 )
      {
        parts = arg.split('=')
        opt = parts[0]
        value = parts[1]
        if( !isNaN(parseInt(value)) || !isNaN(parseFloat(value) ) )
          value = parseInt(value)
      }
      else 
      {
          opt = arg;
          arguments.shift()
          ind = arguments.indexOf(opt)
          if( ind+2 < arguments.length) 
            arg = arguments[arguments.indexOf(opt)+1]
          else arg = ""
          // -opt 
          if( arg.indexOf('-') == 0 || arg === "")
          {
            value = null
          }
          else
          {
          // -opt value
            value = arg;
            if( !isNaN(parseInt(value)) || !isNaN(parseFloat(value) ) )
            {
              value = parseInt(value)
            }
          }
      }
      args[opt] = value;
    }
  }
  ret['args'] = args;
  return ret;
}


function parseCommandLineArgsTypes(commandLine){
  var ret = {};
  var args = {};
  var params = [];

  arguments = commandLine.split(' ');
  ret['cmd'] = arguments.shift()

  for ( var i in arguments )
  {
    var arg = arguments[i];
    var opt = arg;
    var value = "";
    if( arg.indexOf( "-" ) == 0 )
    {
      // -opt=value
      if( arg.indexOf('=') > 0 )
      {
        parts = arg.split('=')
        opt = parts[0]
        value = parts[1]
        if( !isNaN(parseInt(value)) || !isNaN(parseFloat(value) ) )
          value = 'number'
        else
          value = 'string'
      }
      else
      {
          opt = arg;
          arguments.shift()
          ind = arguments.indexOf(opt)
          if( ind+2 < arguments.length)
            arg = arguments[arguments.indexOf(opt)+1]
          else arg = ""
          // -opt
          if( arg.indexOf('-') == 0 || arg === "")
          {
            value = null;
          }
          else
          {
          // -opt value
            value = arg
            if( !isNaN(parseInt(value)) || !isNaN(parseFloat(value) ) )
              value = 'number'
            else
              value = 'string'
          }
      }
      args[opt] = value;
    }
  }
  ret['args'] = args;
  return ret;
}

function parseCommandLineArgsTypesEx(commandLine){
  var ret = {};
  var arglist = [];


  arguments = commandLine.split(' ');
  ret['cmd'] = arguments.shift()

  var argnum = 1;
  for ( var i in arguments )
  {
    var argType = 0;
    var arg = arguments[i];
   var opt = arg;
    var value = "";
    newarg = {'dataType':'','input':'No','argType':0,'name':opt,'javaClass':'org.uc.sidgrid.app.AppParameter','isloop':'No','prompt':opt}
    if( arg.indexOf( "-" ) == 0 )
    {
      // -opt=value
      if( arg.indexOf('=') > 0 )
      {
        parts = arg.split('=')
        opt = parts[0]
        value = parts[1]

        newarg['name'] = opt
        newarg['prompt'] = opt
        if( !isNaN(parseInt(value)) )
          value = 'Integer'
        else if( !isNaN(parseFloat(value) ) )
          value = 'Float'
        else if( value == 'true' || value == 'false' )
          value = 'Boolean'
        else
          value = 'String'

print ("arg = " + arg )
        if( arg[1] === "-" )  argType=dashDashEqualsArgument
        else argType=dashEqualsArgument
print("argType = " + argType )
      }
      else
      {
          ind = arguments.indexOf(opt)
print("ind " + ind + " arg array length " + arguments.length )
          //if( ind+1 < arguments.length)
          //  arg = arguments[arguments.indexOf(opt)+1]
          //else arg = ""
print(" arg is now " + arg )
          // -opt
          if( arg.indexOf('-') == 0 || arg === "")
          {
            value = null;
            argType = switchArgument
print("argType = " + argType )
          }
          else
          {
          // -opt value
            value = arg
            if( !isNaN(parseInt(value)) )
              value = 'Integer'
            else if( !isNaN(parseFloat(value) ) )
              value = 'Float'
            else if( value == 'true' || value == 'false' )
              value = 'Boolean'
            else
              value = 'String'
print("arguments before: " + i + " " + arguments )

print ("arg = " + arg )
            if( arg[1] === "-" )  argType=dashDashSpaceArgument
            else argType=dashSpaceArgument
print("argType = " + argType )

            arguments.splice(i+1,1)
print("arguments after: " +  i + " " + arguments )
          }
      }
      newarg['name'] = opt.replace(/^[-]*/,"")
      newarg['prompt'] = opt.replace(/^[-]*/,"")
      newarg['dataType'] = value
      //if( value == null ) newarg['argType'] = switchArgument
      newarg['argType'] = argType

      argnum++
    }
    else {
      //newarg['name'] = 'unspecified'
      //newarg['prompt'] = 'unspecified'
      newarg['name'] = 'arg' + argnum
      newarg['prompt'] = 'Argument '+ argnum
      value = arg
      if( !isNaN(parseInt(value)) )
        value = 'Integer'
      else if( !isNaN(parseFloat(value) ) )
        value = 'Float'
      else if( value == 'true' || value == 'false' )
        value = 'Boolean'
      else
        value = 'String'
      newarg['dataType'] = value
      newarg['argType'] = valueOnlyArgument
    }
    arglist.push( newarg )
   
  }
  ret['list'] = arglist
  return ret;
}

function parseArgument(arg)
{
    var re_dashDashEquals = new RegExp("^--.*=");
    var re_dashDashSpace = new RegExp("^--.*");
    var re_dashEquals = new RegExp("^-.*=");
    var re_dashSpace = new RegExp("^-.*");
    

    opt = arg
    value = ""
    if( arg[0] === "-" ) {
      nextarg = arguments[0]
      if( arg.indexOf('=')<0 && nextarg && nextarg[0] === "-" ) {
          if( arg[1]==="-" ) argType=dashDashSwitchArgument
          else argType=switchArgument
      } else {
        argType=-1
        if(arg.match(re_dashDashEquals)) {
          argType=dashDashEqualsArgument
          opt=arg.split('=')[0]
          value=arg.split('=')[1]
        } else if(arg.match(re_dashDashSpace)) {
          argType=dashDashSpaceArgument
          value=arguments.shift()
        } else if(arg.match(re_dashEquals)) {
          argType=dashEqualsArgument
          opt=arg.split('=')[0]
          value=arg.split('=')[1]
        } else if(arg.match(re_dashSpace)) {
          argType=dashSpaceArgument
          value=arguments.shift()
        }
      
      }
    } else {
      argType=valueOnlyArgument
      value=arg
    }
    if( !isNaN(parseInt(value)) )
      dataType = 'Integer'
    else if( !isNaN(parseFloat(value) ) )
      dataType = 'Float'
    else if( value == 'true' || value == 'false' )
      dataType = 'Boolean'
    else
      dataType = 'String'

    opt=opt.replace(/^[-]*/,"")
    newarg = {'dataType':dataType,'input':false,'argType':argType,'name':opt,'javaClass':'org.uc.sidgrid.app.AppParameter','isloop':false,'prompt':opt,'default':value}
    return newarg;
}

function parseCommandLineArgsTypesEx2(commandLine){
  var ret = {};
  var arglist = [];
  commandLine=commandLine.replace(/[ ]+/g,' ')
  arguments = commandLine.split(' ');
  ret['cmd'] = arguments.shift()

  while( arguments.length > 0 )
  {
    arg = arguments.shift()
    argobj = parseArgument(arg)
    arglist.push( argobj )
  }

  ret['list'] = arglist;
  return ret;
}


function parseArgument2(arg) {
    var re_dashDashEquals = new RegExp("^--.*=");
    var re_dashDashSpace = new RegExp("^--.*");
    var re_dashEquals = new RegExp("^-.*=");
    var re_dashSpace = new RegExp("^-.*");
    
    

    opt = arg
    value = ""
    prompt = opt
    if( arg[0] === "-" ) {
      if( arg.indexOf('=')<0 && arg.indexOf(' ') <0 ) {
          prompt=arg
          if( arg[1]==="-" ) argType=dashDashSwitchArgument
          else argType=switchArgument
      } else {
        argType=-1
        if(arg.match(re_dashDashEquals)) {
          argType=dashDashEqualsArgument
          opt=arg.split('=')[0] +'=value'
          prompt=arg.split('=')[0]
          value=arg.split('=')[1]
        } else if(arg.match(re_dashDashSpace)) {
          argType=dashDashSpaceArgument
            opt=arg.split(' ')[0]+' value'
            prompt=arg.split(' ')[0]
          value=arg.split(' ')[1]
        } else if(arg.match(re_dashEquals)) {
          argType=dashEqualsArgument
            opt=arg.split('=')[0]+'=value'
            prompt=arg.split('=')[0]
          value=arg.split('=')[1]
        } else if(arg.match(re_dashSpace)) {
          argType=dashSpaceArgument
            opt=arg.split(' ')[0]+' value'
            prompt=arg.split(' ')[0]
            value=arg.split(' ')[1]
        }
      
      }
    } else {
      argType=valueOnlyArgument
      opt=arg
      prompt=arg
      value=arg
    }
    
    if( argType===dashDashSwitchArgument || argType===switchArgument )
    {
        dataType = 'None'
    }
    else 
    {
        if( !isNaN(parseInt(value)) )
          dataType = 'Integer'
        else if( !isNaN(parseFloat(value) ) )
          dataType = 'Float'
        else if( value == 'true' || value == 'false' )
          dataType = 'Boolean'
        else
          dataType = 'String'
    }

    prompt=prompt.replace(/^[-]*/,"")
    //opt=opt.replace(/^[-]*/,"")
    newarg = {'dataType':dataType,'input':'No','argType':argType,'name':prompt,'javaClass':'org.uc.sidgrid.app.AppParameter','isloop':'No','isout':'No','isstdout':'No','prompt':prompt,'value':value,'option':opt}
    return newarg;

}

function parseCommandLineArgsTypesEx3(commandLine){
    ret = {};
    parts = commandLine.split(' -');
    arglist = []
    ret['cmd'] = parts.shift();
    for(i in parts)
    {
        realarg = "-"+parts[i];
        arglist.push( parseArgument2(realarg) );
    }
    ret['list'] = arglist;
    return ret;
}


/* 
 public final static int ValueOnlyArgument=0;
 public final static int SwitchArgument=1;
 public final static int DashEqualsArgument=2;
 public final static int DashSpaceArgument=3;
 public final static int DashDashEqualsArgument=4;
 public final static int DashDashSpaceArgument=5;
 public final static int DashDashSwitchArgument=6;
 
 
 var valueOnlyArgument = 0
 var switchArgument = 1
 var dashEqualsArgument = 2
 var dashSpaceArgument = 3
 var dashDashEqualsArgument = 4
 var dashDashSpaceArgument = 5
 var dashDashSwitchArgument = 6
 
 
 */ 


debug=0;
if(debug) {
    var cmd="vl3opts -t enzoamrgrid   --threshold /share/home/00306/tg455799/ENZO/enzo-b1024-thresh.vth -c /share/home/00306/tg455799/ENZO/enzo-b1024-lut.vct   -r 1920x1080 --showcolortable 0 --samples 1024  --dataMin 0 --dataMax 505.47 -s a.png --longswitcharglast";
    //var ret = parseCommandLineArgsTypesEx2("app valueOnly -switchArg --dashDashSwitch -dashEquals=dashEqualsValue -dashSpace 2.0 --dashDashEquals=2 --dashDashSpace dashDashSpaceValue");
    //var cmd="vl3opts -t enzoamrgrid  -d /work/00306/tg455799/ENZO/HD0302/HD0302:512x512x512:0.5,0.5,0.5 --threshold /share/home/00306/tg455799/ENZO/enzo-b1024-thresh.vth -c /share/home/00306/tg455799/ENZO/enzo-b1024-lut.vct   -r 1920x1080 --showcolortable 0 --samples 1024  --dataMin 0 --dataMax 505.47 -s a.png";
    var ret = parseCommandLineArgsTypesEx2(cmd);
    for(var i in ret['list'])
    {
      o = ret['list'][i]
      print(o['name'] + ' : ' + o['argType'] + ' : ' + o['default'] + ' : ' + o['dataType'] );
    }

    args = ["1","-a","-a=1","-a 1","--a=1","--a 1","--a"]
    for(i in args)
    {
        print( args[i], parseArgument2(args[i]).toSource() );
    }

    print( cmd );
    print( parseCommandLineArgsTypesEx3( cmd ).toSource());
}

