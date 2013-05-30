type file;

type PDB;

type RamaMap;
type RamaIndex;

type OOPSLog;
type SecSeq;
type Fasta;

type RAMAIn {
  RamaMap map;
  RamaIndex index;
  SecSeq secseq;
  Fasta fasta;
  PDB native;
}

type RAMAOut {
  PDB pdb;
  OOPSLog log;
}


app (external e,RAMAOut ramaout) runradialFILE (string prot,RAMAIn ramain){
    runradial prot @ramain.fasta @ramaout.pdb;
}

app runradial (string prot, string fasta, string pdb){
    runradial prot fasta pdb;
}

app runoops (string prot, string fasta, string pdb){
    runoops prot fasta pdb;
}

app runoops_extra_args (string prot, string fasta, string pdb,string st,string tui,string coeff){
    runoops prot fasta pdb st tui coeff;
}

(external e1) finished(external e0)
{
   trace("all done");
}

(external e0) initex()
{
  trace("initex called");
}

app (external eo) broadcast (external ei, string fname)
{
  bc2all fname;
}

main()
{
    string plistfile=@arg("plist","");
    string indir=@arg("indir","");
    string outdir=@arg("outdir","output");
    string nsims=@arg("nsims","1");

    string st=@arg("st","100");
    string tui=@arg("tui","100");
    string coeff=@arg("coeff","0.1");

    int create=1;

    string plist[] = readData(plistfile);

    int simulations[]=[ 0 : @toint(nsims) -1 ];

    RAMAIn ramain[] <ext; exec="RAMAInProts.map.sh",i=indir,p=plistfile>;
    RAMAOut ramaout[][] <ext;exec="RandProtRadialMapper.py",o=outdir,p=plistfile,n=nsims,c=create>;
 
    foreach sim in simulations {
    	 foreach prot,index in plist {
              runoops_extra_args(prot,@strcat("/",@filename(ramain[index].fasta)),@strcat("/",@filename(ramaout[index][sim].pdb)),st,tui,coeff );
         }
    }
}

main();
