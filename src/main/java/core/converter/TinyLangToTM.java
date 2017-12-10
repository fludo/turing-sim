/*
 *   Copyright (©) 2009 | 16 January 2009 | EPFL (Ecole Polytechnique fédérale de Lausanne)
 *
 *   TuringSim is free software ; you can redistribute it and/or modify it under the terms of the
 *   GNU General Public License as published by the Free Software Foundation ; either version 3 of
 *   the License, or (at your option) any later version.
 *
 *   TuringSim is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY ;
 *   without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License along with TuringSim ;
 *   if not, write to the Free Software Foundation,
 *
 *   Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 *
 *
 *   Author : Ludovic Favre <ludovic.favre@epfl.ch>
 *
 *   Project supervisor : Mahdi Cheraghchi <mahdi.cheraghchi@epfl.ch>
 *
 *   Web site : http://icwww.epfl.ch/~lufavre
 *
 */

/*
 * General rawInput format :
 * MAIN(){
    INIT(X,9);
    INIT(A,1);
    INIT(B,0);
    INIT(C,3);
    INC(X);
    ODD(X);
    JUMPR(LB);
    PRINT(A);
    GOTO(END);
    LABEL_LC;
    PRINT(C);
    GOTO(END);
    LABEL_LB;
    PRINT(B);
    GOTO(LC);
    GOTO(END);
    LABEL_END;
    };
 *
 * Note that routine call are not totally fixed at the moment.
 * Once initialized (to an integer), every variable is memorized as a binary string
 *
 */
package core.converter;

import core.exception.InstructionParameterException;
import core.exception.VariableAlreadyExistsException;
import core.exception.VariableNotFoundException;
import core.lang.instructions.Add;
import core.lang.instructions.CustomFunction;
import core.lang.instructions.Dec;
import core.lang.instructions.Equ;
import core.lang.instructions.Even;
import core.lang.instructions.Goto;
import core.lang.instructions.ITypes;
import core.lang.instructions.Inc;
import core.lang.instructions.Init;
import core.lang.instructions.Jumpa;
import core.lang.instructions.Jumpar;
import core.lang.instructions.Jumpr;
import core.lang.instructions.Label;
import core.lang.instructions.Odd;
import core.lang.instructions.Sub;
import core.lang.instructions.TInstruction;
import gui.TinyLangPanel;
import java.lang.String;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.logging.Logger;
import logging.Log;

/**
 *
 * @author Ludovic Favre
 */
public class TinyLangToTM {

    protected static Logger log = Logger.getLogger(Log.FILENAME);
    protected static boolean debug = false; // default disabled
    private String rawInput_;
    public static HashMap<String, String> varContext = new HashMap<String, String>();
    public static HashMap<String, String> routineList = new HashMap<String, String>();
    public static HashMap<String, Integer> labelList = new HashMap<String, Integer>();
    private static String currentVariable = "";
    private TinyLangPanel tlp_;
    private static boolean lastResult;
    private static int MAX_INSTR = 1000;
    private int niter = 0;

    public TinyLangToTM(TinyLangPanel tlp, String rawInput) {
        this.rawInput_ = rawInput;
        this.tlp_ = tlp;
    }

    public void inputIs(String rawInput) {
        this.niter = 0;
        varContext = new HashMap<String, String>();
        routineList = new HashMap<String, String>();
        labelList = new HashMap<String, Integer>();
        this.rawInput_ = rawInput;
    }

    @Deprecated
    public static void main(String args[]) {
        String input = "\n" +
                "MAIN(){\n" +
                "INIT(TEST,20);\n" +
                "SUBI(TEST,10);\n" +
                "CALL_MYROUTINE;\n" + // may or not terminate the current routine
                "EQU(TEST,TEST);" + // this instruction can accept or reject -> tm.. should terminate the routine
                "PRINT(TEST);" +
                "};\n\n" +
                "ROUTINE(MYROUTINE){\n" + // should init param1 in it...
                "PRINT(TEST);" +
                "\n};\n" +
                "ROUTINE(MYROUTINE1){\n" + // should init param1 in it...
                "PRINT(TEST);" +
                "\n};\n" +
                "\t";

        /* Other possibilities :
        FOR(Integer){ ... };
        SUB(Variable,Integer);
        ADD(Variable,Integer);
        INIT(Variable,Integer); */
        System.out.println("Input : \n" + input);
        System.out.println("--------------- PARSE --------------");
        TinyLangToTM parser = new TinyLangToTM(null, input);
        try {
            parser.parse();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void parse() throws Exception {
        print("Starting parsing...");
        try {
            Pattern mainSplitRegex =
                    Pattern.compile("(MAIN\\(\\)\\{[a-zA-Z0-9_\\(\\);, \n\t\\s]*\\});");
            Pattern routineRegex =
                    Pattern.compile("(MAIN\\(\\)\\{[a-zA-Z0-9_\\(\\);, \n\t\\s]*\\});[\n\t\\s]*((ROUTINE\\([a-zA-Z1-9,]*\\)\\{[a-zA-Z0-9;()\\s\n]*\\};[\n\t\\s]*)+)");
            Matcher regexMatcher = mainSplitRegex.matcher(this.rawInput_);
            Matcher routineMatcher = routineRegex.matcher(this.rawInput_);
            //print("Matching...");
            /* while (regexMatcher.find()) {
            for (int i = 1; i <= regexMatcher.groupCount(); i++) {
            // matched text: regexMatcher.group(i)
            // match start: regexMatcher.start(i)
            // match end: regexMatcher.end(i)
            //System.out.println(regexMatcher.start(i));
            print(regexMatcher.group(i));
            print("***************************");
            //System.out.println(regexMatcher.end(i));
            }
            print("Adding routines...");
            parseSubRoutine(regexMatcher.group(2));
            print("Parsing main...");
            parseMain(regexMatcher.group(1));
             * }
             */
            if (routineMatcher.find()) {

                try{
                print("Adding routines...");
                Pattern routineSingleRegex =
                        Pattern.compile("(ROUTINE\\([a-zA-Z1-9,]*\\)\\{[a-zA-Z0-9;()\\s\n]*\\};[\n\t\\s]*)");
                Matcher multiRoutineMatcher = routineSingleRegex.matcher(routineMatcher.group(2));
                //parseSubRoutine(routineMatcher.group(2));
                while(multiRoutineMatcher.find()){
                    parseSubRoutine(multiRoutineMatcher.group());
                }
                }
                catch(Exception e){
                    print("No routine found");
                }
                print("Parsing main...");
                print("------------------------------------");
                parseMain(routineMatcher.group(1));
            }

            else if(regexMatcher.find()){
                print("Parsing main...");
                print("------------------------------------");
                parseMain(regexMatcher.group(1));
            }
            else {
                print("The syntax is invalid, parsing failed !");
            }
        } catch (PatternSyntaxException ex) {
            print("An error occured : " + ex.getMessage());
            throw new Exception("An error occured : " + ex.getMessage());
        // Syntax error in the regular expression
        }



    }

    private void parseMain(String mainString) throws Exception {
        //System.out.println("PARSEMAIN : \n" + mainString);
        // isolate the main instructions :
        Pattern mainInstrRegex = Pattern.compile("MAIN\\(\\)\\{([a-zA-Z0-9_\\(\\);, \n\t\\s]*)\\}[;\n\t\\s]*");
        Matcher regexMatcher = mainInstrRegex.matcher(mainString);
        regexMatcher.find();
        mainString = regexMatcher.group(1);
        //print("Got main strings " + mainString);
        String[] instr = (mainString.replaceAll("\\s\n", "")).split(";");

        //LinkedList<TInstruction> instructions = new LinkedList<TInstruction>();
        parseLabels(instr);     
        for (int i = 0; i < instr.length; i++) {
            if(niter>MAX_INSTR){
                break;
            }
            instr[i] = instr[i].replaceAll("\n", "").replaceAll("\\s", "");
            
            TInstruction currInstr = null;
            try {
                if(instr[i].equals("")){
                    i++;
                    continue;
                }
                currInstr = this.parseInstruction(instr[i]);
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("Error while parsing instruction : (" + i + ") " + instr[i] + "\n" + e.getMessage());
            }

            if (currInstr instanceof Init) {
                Init initInstr = (Init) currInstr;
                try {
                    this.initVariable(initInstr.variable(), initInstr.value());
                } catch (Exception e) {
                    e.printStackTrace();
                    errorPrint("Error while trying to initialize variable : " + initInstr.variable());
                }

            }
            if (currInstr != null) {
                currInstr.execute();
                if (currInstr.accepts()) {

                    print(instr[i] + "-> accept");
                    // apply changes to variables
                    if (currInstr instanceof Add  || currInstr instanceof Sub || currInstr instanceof Dec || currInstr instanceof Inc) {
                        
                        lastResult = true;
                        varContext.put(currentVariable, currInstr.output());
                        //print("Updated " + currentVariable);
                    }
                    if (currInstr instanceof Odd || currInstr instanceof Even) {

                        lastResult = currInstr.accepts();
                    }

                    if (currInstr instanceof Label) {
                        /*lastResult = true;
                        labelList.put(((Label) currInstr).label(), new Integer(i));
                        print("Added label :" + ((Label)currInstr).label());*/
                    }
                    if (currInstr instanceof Jumpar) {
                        if (lastResult == true) {
                            int newI = labelList.get(((Jumpar) currInstr).labelAccept()).intValue();
                            i = newI ;
                            lastResult = true;
                        } else {
                            int newI = labelList.get(((Jumpar) currInstr).labelReject()).intValue();
                            i = newI ;
                            lastResult = true;
                        }
                    }
                    if (currInstr instanceof Jumpa) {
                        if (lastResult != false) {
                            int newI = labelList.get(((Jumpa) currInstr).label()).intValue();
                            i = newI ;
                            lastResult = true;
                        }
                    }
                    if (currInstr instanceof Jumpr) {
                        // System.out.println("Jumpr, lastres is "+lastResult);
                        if (lastResult == false) {
                            int newI = labelList.get(((Jumpr) currInstr).label()).intValue();
                            // System.out.println("Going to label : "+((Jumpr)currInstr).label()+" line "+newI);
                            i = newI ;
                            lastResult = true;
                        }
                    }
                    if (currInstr instanceof Goto) {
                        lastResult = true;
                        int newI = labelList.get(((Goto) currInstr).label()).intValue();
                        i = newI ;
                        
                    }
                } else {
                    lastResult = false;
                    print(instr[i] + "-> reject");
                }
            }
        }
    }

     private void parseLabels(String[] instr) throws Exception {
        Pattern initRegex = Pattern.compile("LABEL_([a-zA-Z0-9]+)");
        for (int i = 0; i < instr.length; i++) {
            if(niter>MAX_INSTR){
                break;
            }
            instr[i] = instr[i].replaceAll("\n", "");
            

            try {
                if(instr[i].equals("")){
                    i++;
                    continue;
                }

                Matcher regexMatcher = initRegex.matcher(instr[i]);
                if(regexMatcher.find()){
                String[] labelName = new String[1];
                labelName[0] = regexMatcher.group().replaceAll("LABEL_", "");
                labelList.put(labelName[0], new Integer(i));
                print("Added Label : "+labelName[0]);
                }
            } catch (Exception e) {
                throw new Exception("Error while label : (" + i + ") " + instr[i] + "\n" + e.getMessage());
            }
        }
    }             

    private TInstruction parseInstruction(String instruction) throws Exception {
        this.niter ++;
        /* Recalle of instructions 
         * FOR(Integer){ ...:... }; // do ... a certain amount of time
         * SUB(Variable,Integer);
         * ADD(Variable,Integer);
         * INIT(Variable,Integer); // has to be called before using Variable..
         * INT/DEC(Variable) // decrement the variable
         */
        try {
            if (instruction.startsWith("SUB(")) {
                Pattern initRegex = Pattern.compile("SUB\\(([a-zA-Z0-9]+,[a-zA-Z0-9]+)\\)");
                Matcher regexMatcher = initRegex.matcher(instruction);
                regexMatcher.find();
                String initParams[] = regexMatcher.group().replaceAll("SUB\\(", "").replaceAll("\\)", "").split(",");
                currentVariable = initParams[0];
                initParams[0] = varContext.get(initParams[0]);
                initParams[1] = varContext.get(initParams[1]);
                ITypes[] t = {ITypes.Variable, ITypes.Variable};
                if (debug) {
                    log.info("Created sub instruction : " + instruction);
                }
                return new Sub(initParams, t);

            } else if (instruction.startsWith("SUBI(")) {
                Pattern initRegex = Pattern.compile("SUBI\\(([a-zA-Z0-9]+,[0-9]+)\\)");
                Matcher regexMatcher = initRegex.matcher(instruction);
                regexMatcher.find();
                String initParams[] = regexMatcher.group().replaceAll("SUBI\\(", "").replaceAll("\\)", "").split(",");
                currentVariable = initParams[0];
                initParams[0] = varContext.get(initParams[0]);
                initParams[1] = TinyLangUtils.intToBinary(Integer.parseInt(initParams[1]));
                ITypes[] t = {ITypes.Variable, ITypes.Variable};
                if (debug) {
                    log.info("Created sub instruction : " + instruction);
                }
                return new Sub(initParams, t);

            } else if (instruction.startsWith("ADD(")) {
                Pattern initRegex = Pattern.compile("ADD\\(([a-zA-Z0-9]+,[a-zA-Z0-9]+)\\)");
                Matcher regexMatcher = initRegex.matcher(instruction);
                regexMatcher.find();
                String initParams[] = regexMatcher.group().replaceAll("ADD\\(", "").replaceAll("\\)", "").split(",");
                currentVariable = initParams[0];
                initParams[0] = varContext.get(initParams[0]);
                initParams[1] = varContext.get(initParams[1]);
                ITypes[] t = {ITypes.Variable, ITypes.Variable};
                if (debug) {
                    log.info("Created add instruction : " + instruction);
                }
                return new Add(initParams, t);

            }else if (instruction.startsWith("EQU(")) {
                Pattern initRegex = Pattern.compile("EQU\\(([a-zA-Z0-9]+,[a-zA-Z0-9]+)\\)");
                Matcher regexMatcher = initRegex.matcher(instruction);
                regexMatcher.find();
                String initParams[] = regexMatcher.group().replaceAll("EQU\\(", "").replaceAll("\\)", "").split(",");
                currentVariable = initParams[0];
                initParams[0] = varContext.get(initParams[0]);
                initParams[1] = varContext.get(initParams[1]);
                ITypes[] t = {ITypes.Variable, ITypes.Variable};
                if (debug) {
                    log.info("Created add instruction : " + instruction);
                }
                return new Equ(initParams, t);

            }
            else if (instruction.startsWith("ADDI(")) {
                Pattern initRegex = Pattern.compile("ADDI\\(([a-zA-Z0-9]+,[0-9]+)\\)");
                Matcher regexMatcher = initRegex.matcher(instruction);
                regexMatcher.find();
                String initParams[] = regexMatcher.group().replaceAll("ADDI\\(", "").replaceAll("\\)", "").split(",");
                currentVariable = initParams[0];
                initParams[0] = varContext.get(initParams[0]);
                initParams[1] = TinyLangUtils.intToBinary(Integer.parseInt(initParams[1]));
                ITypes[] t = {ITypes.Variable, ITypes.Integer};
                if (debug) {
                    log.info("Created add instruction : " + instruction);
                }
                return new Add(initParams, t);

            } else if (instruction.startsWith("INC(")) {
                //System.out.println("PARSING INC : >"+instruction+"<");
                Pattern initRegex = Pattern.compile("INC\\(([a-zA-Z0-9]+)\\)");
                Matcher regexMatcher = initRegex.matcher(instruction);
                //System.out.println("matcher built");
                boolean res = regexMatcher.find();
                //System.out.println("find executed"+res);
                String initParams[] = new String[1];
                initParams[0] = regexMatcher.group().replaceAll("INC\\(","").replaceAll("\\)","");
                
                //System.out.println("group executed");
                currentVariable = initParams[0];
                initParams[0] = varContext.get(initParams[0]);
                ITypes[] t = {ITypes.Variable};
                if (debug) {
                    log.info("Created inc instruction : " + instruction);
                }
                return new Inc(initParams, t);

            } else if (instruction.startsWith("DEC(")) {
                Pattern initRegex = Pattern.compile("DEC\\(([a-zA-Z0-9]+)\\)");
                Matcher regexMatcher = initRegex.matcher(instruction);
                regexMatcher.find();
                String initParams[] = new String[1];
                initParams[0] = regexMatcher.group().replaceAll("DEC\\(","").replaceAll("\\)","");
                currentVariable = initParams[0];
                initParams[0] = varContext.get(initParams[0]);
                ITypes[] t = {ITypes.Variable};
                if (debug) {
                    log.info("Created dec instruction : " + instruction);
                }
                return new Dec(initParams, t);

            } else if (instruction.startsWith("ODD(")) {
                Pattern initRegex = Pattern.compile("ODD\\(([a-zA-Z0-9]+)\\)");
                Matcher regexMatcher = initRegex.matcher(instruction);
                regexMatcher.find();
                String initParams[] = new String[1];
                initParams[0] = regexMatcher.group().replaceAll("ODD\\(","").replaceAll("\\)","");
                currentVariable = initParams[0];
                initParams[0] = varContext.get(initParams[0]);
                ITypes[] t = {ITypes.Variable};
                if (debug) {
                    log.info("Created Odd instruction : " + instruction);
                }
                return new Odd(initParams, t);

            } else if (instruction.startsWith("EVEN(")) {
                Pattern initRegex = Pattern.compile("EVEN\\(([a-zA-Z0-9]+)\\)");
                Matcher regexMatcher = initRegex.matcher(instruction);
                regexMatcher.find();
                String initParams[] = new String[1];
                initParams[0] = regexMatcher.group().replaceAll("EVEN\\(","").replaceAll("\\)","");
                currentVariable = initParams[0];
                initParams[0] = varContext.get(initParams[0]);
                ITypes[] t = {ITypes.Variable};
                if (debug) {
                    log.info("Created Even instruction : " + instruction);
                }
                return new Even(initParams, t);

            }  else if (instruction.startsWith("INIT(")) {
                // INIT(Variable,Integer)
                // add variable to its environnement
                Pattern initRegex = Pattern.compile("INIT\\(([a-zA-Z0-9]+,[0-9]+)\\)");
                Matcher regexMatcher = initRegex.matcher(instruction);
                regexMatcher.find();
                String initParams[] = regexMatcher.group().replaceAll("INIT\\(", "").replaceAll("\\)", "").split(",");
                ITypes[] t = {ITypes.Variable, ITypes.Integer};
                if (debug) {
                    log.info("Initialization of variable : " + instruction);
                }
                return new Init(initParams, t);

            } else if (instruction.startsWith("CALL_")) {
                if (debug) {
                    log.info("Custom function call : " + instruction);
                }
                Pattern initRegex = Pattern.compile("CALL_([a-zA-Z0-9]+)");
                Matcher regexMatcher = initRegex.matcher(instruction);
                regexMatcher.find();
                String[] labelName = new String[1];
                labelName[0] = regexMatcher.group().replaceAll("CALL_", "");

                runRoutineCode(labelName[0]);
                return new CustomFunction(labelName[0]);
            // custom function call
            } else if (instruction.startsWith("PRINT(")) {
                if (debug) {
                    log.info("Custom variable printing : " + instruction);
                }
                Pattern initRegex = Pattern.compile("PRINT\\(([a-zA-Z0-9]+)\\)");
                Matcher regexMatcher = initRegex.matcher(instruction);
                regexMatcher.find();
                String varName = regexMatcher.group().replaceAll("PRINT\\(", "").replaceAll("\\)", "");
                String varValue = varContext.get(varName);
                // System.out.println("*************Value of "+varName+" is "+varValue);
                int value = TinyLangUtils.binaryToInt(varValue);
                print("Value of : " + varName + " is " + varValue + " (int value is : " + value + ")");

                return null;
            // custom function call
            } else if (instruction.startsWith("LABEL_")) {
                if (debug) {
                    log.info("Label : " + instruction);
                }

                Pattern initRegex = Pattern.compile("LABEL_([a-zA-Z0-9]+)");
                Matcher regexMatcher = initRegex.matcher(instruction);
                regexMatcher.find();
                String[] labelName = new String[1];
                labelName[0] = regexMatcher.group().replaceAll("LABEL_", "");

                return new Label(labelName, null);
            } else if (instruction.startsWith("JUMPAR(")) {
                if (debug) {
                    log.info("Jumpar : " + instruction);
                }

                Pattern initRegex = Pattern.compile("JUMPAR\\(([a-zA-Z0-9,]+)\\)");
                Matcher regexMatcher = initRegex.matcher(instruction);
                regexMatcher.find();
                String[] labels = regexMatcher.group().replaceAll("JUMPAR\\(", "").replaceAll("\\)", "").split(",");

                return new Jumpar(labels, null);
            } else if (instruction.startsWith("JUMPR(")) {
                if (debug) {
                    log.info("Jumpr : " + instruction);
                }

                Pattern initRegex = Pattern.compile("JUMPR\\(([a-zA-Z0-9]+)\\)");
                Matcher regexMatcher = initRegex.matcher(instruction);
                regexMatcher.find();
                String labels[] = new String[1];
                labels[0] = regexMatcher.group().replaceAll("JUMPR\\(", "").replaceAll("\\)", "");

                return new Jumpr(labels, null);
            } else if (instruction.startsWith("JUMPA(")) {
                if (debug) {
                    log.info("Jumpa : " + instruction);
                }

                Pattern initRegex = Pattern.compile("JUMPA\\(([a-zA-Z0-9]+)\\)");
                Matcher regexMatcher = initRegex.matcher(instruction);
                regexMatcher.find();
                String labels[] = new String[1];
                labels[0] = regexMatcher.group().replaceAll("JUMPA\\(", "").replaceAll("\\)", "");

                return new Jumpa(labels, null);
            } else if (instruction.startsWith("GOTO(")) {
                if (debug) {
                    log.info("Goto : " + instruction);
                }

                Pattern initRegex = Pattern.compile("GOTO\\(([a-zA-Z0-9,]+)\\)");
                Matcher regexMatcher = initRegex.matcher(instruction);
                regexMatcher.find();
                String labels[] = new String[1];
                labels[0] = regexMatcher.group().replaceAll("GOTO\\(", "").replaceAll("\\)", "");

                return new Goto(labels, null);
            } else {
                log.warning("Invalid instruction : >" + instruction + "<");
            }
        } catch (InstructionParameterException e) {
            errorPrint("An error occured while trying to build the language blocks : " + e.getMessage());
        }
        return null;
    }

    private void parseSubRoutine(String routineString) {
        // System.out.println("SubRoutine : \n" + routineString);
        // isolate the main instructions :
        Pattern routInstrRegex = Pattern.compile("ROUTINE\\(([a-zA-Z0-9]+)\\)\\{([a-zA-Z0-9_\\(\\);, \n\t\\s]*)\\}[;\n\t\\s]*");

        Matcher regexMatcher = routInstrRegex.matcher(routineString);
        regexMatcher.find();
        String routineName = regexMatcher.group(1);

        String routineCode = regexMatcher.group(2);
        print("Adding routine : " + routineName);
       // print("Code : ->" + routineCode + "<-");
        routineList.put(routineName, routineCode);


    /*Matcher regexMatcher = mainInstrRegex.matcher(mainString);
    regexMatcher.find();
    mainString = regexMatcher.group(1);
    System.out.println("Got main strings " + mainString);
    String[] instr = (mainString.replaceAll("\\s\n", "")).split(";");

    LinkedList<TInstruction> instructions = new LinkedList<TInstruction>();
    for (int i = 0; i < instr.length; i++) {
    instr[i] = instr[i].replaceAll("\n", "");
    
    TInstruction currInstr = null;
    try {
    currInstr = this.parseInstruction(instr[i]);
    } catch (Exception e) {
    throw new Exception("Error while parsing instruction : (" + i + ") " + instr[i] + "\n" + e.getMessage());
    }

    if (currInstr instanceof Init) {
    Init initInstr = (Init) currInstr;
    try {
    this.initVariable(initInstr.variable(), initInstr.value());
    } catch (Exception e) {
    e.printStackTrace();
    System.err.println("Error while trying to initialize variable : " + initInstr.variable());
    }

    }
    if (currInstr != null) {
    if (currInstr.accepts()) {
    System.out.println(instr[i] + "-> accept");
    // apply changes to variables
    if (currInstr instanceof Add || currInstr instanceof Sub || currInstr instanceof Dec || currInstr instanceof Inc) {
    varContext.put(currentVariable, currInstr.output());
    System.out.println("Updated " + currentVariable);
    }
    } else {
    System.out.println(instr[i] + "-> reject");
    }
    }
    }*/
    }

    private void runRoutineCode(String routineName) throws Exception {
        String codeString = routineList.get(routineName);
        // System.out.println("Executing routine "+routineName+codeString);
        String[] instr = (codeString.replaceAll("\\s\n", "")).split(";");

        for (int i = 0; i < instr.length; i++) {
            instr[i] = instr[i].replaceAll("\n", "");
            
            TInstruction currInstr = null;
            try {
                if(instr[i].equals("")){
                    i++;
                    continue;
                }
                currInstr = this.parseInstruction(instr[i]);
            } catch (Exception e) {
                throw new Exception("Error while parsing instruction : (" + i + ") " + instr[i] + "\n" + e.getMessage());
            }

            if (currInstr instanceof Init) {
                Init initInstr = (Init) currInstr;
                try {
                    this.initVariable(initInstr.variable(), initInstr.value());
                } catch (Exception e) {
                    e.printStackTrace();
                    errorPrint("Error while trying to initialize variable : " + initInstr.variable());
                }

            }
            if (currInstr != null) {
                currInstr.execute();
                if (currInstr.accepts()) {
                    print(instr[i] + "-> accept");
                    // apply changes to variables
                    if (currInstr instanceof Add || currInstr instanceof Sub || currInstr instanceof Dec || currInstr instanceof Inc) {
                        varContext.put(currentVariable, currInstr.output());
                       // print("Updated " + currentVariable);
                    }
                } else {
                    print(instr[i] + "-> reject");
                }
            }
        }
    }

    /**
     * Initialize a variable in the context to default (value 0)
     * @param variableSymbol
     * @throws core.exception.VariableAlreadyExistsException
     */
    private void initVariable(String variableSymbol) throws VariableAlreadyExistsException {
        if (varContext.containsKey(variableSymbol)) {
            throw new VariableAlreadyExistsException("Variable : " + variableSymbol + " has already been initialized !");
        }

        varContext.put(variableSymbol, "0");
    }

    /**
     * Initialize a variable in the context. Value will be translated to binary
     * @param variableSymbol
     * @param value as an String representing the integer
     * @throws core.exception.VariableAlreadyExistsException
     */
    private void initVariable(String variableSymbol, String value) throws VariableAlreadyExistsException {
        if (varContext.containsKey(variableSymbol)) {
            throw new VariableAlreadyExistsException("Variable : " + variableSymbol + " has already been initialized !");
        }

        varContext.put(variableSymbol, TinyLangUtils.intToBinary(Integer.parseInt(value)));

    }

    /**
     * Update the value of the variable
     * @param variableSymbol
     * @param value
     * @throws core.exception.VariableAlreadyExistsException
     */
    private void updateVariable(String variableSymbol, String value) throws VariableAlreadyExistsException {
        if (!varContext.containsKey(variableSymbol)) {
            throw new VariableAlreadyExistsException("Variable : " + variableSymbol + " cannot be found !");
        }

        varContext.put(variableSymbol, TinyLangUtils.intToBinary(Integer.parseInt(value)));

    }

    /**
     * Drop a variable from the context (delete it)
     * @param variableSymbol
     * @throws VariableNotFoundException
     */
    private void forgetVariable(String variableSymbol) throws VariableNotFoundException {
        if (null == varContext.remove(variableSymbol)) {
            throw new VariableNotFoundException("Symbol " + variableSymbol + " doesn't exist");
        }
    }

    /**
     * <p> Printing utility function
     * @param text
     */
    private void print(String text) {
        if (this.tlp_ != null) {
            this.tlp_.appendLog(text);
        } else {
            System.out.println(text);
        }
    }

    /**
     * <p> Error printing utility function
     * @param text
     */
    private void errorPrint(String text) {
        if (this.tlp_ != null) {
            this.tlp_.appendLog("ERROR : \n" + text);
        } else {
            System.err.println(text);
        }
    }
}
