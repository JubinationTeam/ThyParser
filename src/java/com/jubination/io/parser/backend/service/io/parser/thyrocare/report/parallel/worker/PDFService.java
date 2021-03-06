package com.jubination.io.parser.backend.service.io.parser.thyrocare.report.parallel.worker;

import com.jubination.io.parser.model.pojo.report.Test;
import com.jubination.io.parser.model.pojo.report.Report;
import com.jubination.io.parser.model.pojo.report.Profile;
import com.jubination.io.parser.model.pojo.report.ReferenceRange;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class PDFService {
                            
                          private boolean hemoFlag=false;
                          private Profile profileArthritis =new Profile("ARTHRITIS");
                           private Profile profileHeart =new Profile("HEART RISKY MARKERS");
                           private Profile profileBlood =new Profile("COMPLETE BLOOD COUNT");
                           private  Profile profileDiabetes =new Profile("DIABETES");
                           private  Profile profileElectrolytes =new Profile("ELECTROLYTES");
                           private  Profile profileIron =new Profile("IRON");
                           private  Profile profileLipid =new Profile("CHOLESTEROL");
                            private  Profile profileLiver =new Profile("LIVER");
                             private Profile profileKidney =new Profile("KIDNEY");
                            private   Profile profileThyroid =new Profile("THYROID");
                           private  Profile profileToxic =new Profile("TOXIC ELEMENTS");
                            private  Profile profileVitamins =new Profile("VITAMINS");
                            private  Profile profilePancreas =new Profile("PANCREAS");
                            private Profile profileHormones =new Profile("HORMONES");
                            private Profile profileProstate =new Profile("PROSTATE");
                            private Profile profileStress =new Profile("STRESS");
                             private Profile profileHepatitis =new Profile("HEPATITIS");
                            private boolean flagArthritis =false;
                           private boolean flagHeart =false;
                           private boolean flagBlood =false;
                            private boolean flagDiabetes =false;
                            private boolean flagElectrolytes =false;
                            private boolean flagIron =false;
                            private boolean flagLipid =false;
                             private boolean flagLiver =false;
                             private boolean flagKidney =false;
                             private boolean flagThyroid =false;
                           private boolean flagToxic =false;
                             private boolean flagVitamins =false;
                             private boolean flagPancreas =false;
                            private boolean flagHormones =false;
                            private boolean flagProstate =false;
                            private boolean flagStress =false;
                             private boolean flagHepatitis =false;

                           //TEST NAME DETERMINATION SERVICE
                           public  String getTestName(String line){
                                        Pattern pattern = Pattern.compile("[A-Z]+");
                                        Matcher matcher = pattern.matcher(line);

                                        //EXCEPTION - HEMO CHECK
                                            if(isHemoTest(line)){
                                                        return line;
                                            }
                                        /////////////
                                        
                                        //ADD OTHER EXCEPTIONS IN TEST NAMES 
                                        if(line.startsWith("HbA1c")||line.startsWith("Lipoprotein")||line.startsWith("DHEA - SULPHATE (DHEAS)")||line.startsWith("URI. ALBUMIN/CREATININE RATIO (UA/C)")){
                                                        return line;
                                        }
                                        /////////////

                                        //REMOVE UNWANTED SHORT FORMS AND SMALL CASED WORDS IN TEST NAMES
                                         if(line.length()>=7){
                                                        Pattern patternRemove = Pattern.compile("[A-Z0-9][A-Z0-9][A-Z0-9]([A-Z0-9]?) - ");
                                                        Matcher matcherRemove = patternRemove.matcher(line.substring(0, 7));
                                                        Pattern patternRemove2 = Pattern.compile("([a-z][a-z][a-z][a-z][a-z][a-z][a-z][a-z])+");
                                                        Matcher matcherRemove2 = patternRemove2.matcher(line);
                                                        if(matcher.find()&&!matcherRemove.find()&&!matcherRemove2.find()&&!line.contains("SUGGEST")&&!line.contains("suggest")&&!line.contains("Suggest")){
                                                                        String group=matcher.group();
                                                                        if(group.length()>1){
                                                                                        return group;
                                                                        }
                                                          }
                                         }
                                         /////////////
                                         
                                         //REMOVE UNWANTED WORDS IN TEST NAME
                                         else if(matcher.find()&&!line.contains("SUGGEST")&&!line.contains("suggest")&&!line.contains("Suggest")){
                                                        String group=matcher.group();
                                                        if(group.length()>1){
                                                                        return group;
                                                        }
                                           }
                                         //////////////

                                        return null;
                             }
                             ///////////////////////////////////////////////

                            //REFERENCES FROM TEST METHOD SERVICE
                            public  String[] getReferencesFromTestMethod(String line) {
                                        String[] val =new String[2];
                                        String ref="";
                                        String[] regexArray={"M:","Male:","Male :",">","<"};
                                        String[] complexRegexArray={"[\\d+]([\\.\\d+]*)[\\s*]-[\\s*][\\d+]([\\.\\d+]*)",
                                                                                    "[\\d+]([\\.\\d+]*)[\\s*]-[\\s*][\\d+]([\\.\\d+\\.])[\\d+]",
                                                                                    "[\\d+]([\\.\\d+]*)-[\\d+]([\\.\\d+]*)",
                                                                                    "[\\d+]([\\.\\d+]*)  -  [\\d+]([\\.\\d+]*)",
                                                                                    "[\\d+]([\\.\\d+]*) -  [\\d+]([\\.\\d+]*)"};
                                        
                                        //EXCEPTION - HEMO GROUP TESTNAME, REFERENCE RANGE AND VALUE DETERMINATION//
                                        if(hemoFlag){
                                                        String[] hemoVals=hemoTestParameterDetermination(line);
                                                        return hemoVals;
                                        }
                                        
                                        //EXCEPTION - RHEUMATOID FACTOR (RF)//
                                        if(line.contains("RHEUMATOID FACTOR (RF)")){
                                            val[0]=line;
                                            return val;
                                        }
                                        
                                        //EXCEPTION - TOTAL THYROXINE//
                                        if(line.contains("TOTAL THYROXINE (T4)")){
                                                                    String[] thyroVal=new String[3];
                                                                    Pattern pattern = Pattern.compile("\\d\\.\\d - \\d\\d\\.\\d");
                                                                    Matcher matcher = pattern.matcher(line);
                                                                    if(matcher.find()){
                                                                                    thyroVal[0]=line.split("\\d\\.\\d - \\d\\d\\.\\d")[0];
                                                                                    thyroVal[1]=matcher.group(0);
                                                                                    thyroVal[2]=line.split("\\d\\.\\d - \\d\\d\\.\\d")[1];
                                                                                    return thyroVal;
                                                                    }
                                        }
                                        
                                        //EXCEPTION TOTAL TRIIODOTHYRONINE//
                                        if(line.contains("TOTAL TRIIODOTHYRONINE")){
                                                                    String[] thyroVal=new String[3];
                                                                    Pattern pattern = Pattern.compile("\\d\\d - \\d\\d\\d");
                                                                    Matcher matcher = pattern.matcher(line);
                                                                    if(matcher.find()){
                                                                                    thyroVal[0]=line.split("\\d\\d - \\d\\d\\d")[0];
                                                                                    thyroVal[1]=matcher.group(0);
                                                                                    thyroVal[2]=line.split("\\d\\d - \\d\\d\\d")[1];
                                                                                    return thyroVal;
                                                                    }
                                        }
                                        
                                        //ALPHA DIFFERENTIATION//
                                        for (String regexVal : regexArray) {
                                                        if (line.split(regexVal).length == 2) {
                                                                        ref = regexVal + line.split(regexVal)[1];
                                                                        val[0] = line.split(regexVal)[0];
                                                                        val[1]=ref;
                                                                        return val;
                                                        }
                                        }
                                        
                                       
                                        //NUMERIC DIFFERENTIATION//
                                        for(String complexRegexVal:complexRegexArray){
                                                        if(line.split(complexRegexVal).length==2){
                                                                    Pattern pattern = Pattern.compile(complexRegexVal);
                                                                    Matcher matcher = pattern.matcher(line);
                                                                    if(matcher.find()){
                                                                                    ref=matcher.group(0)+line.split(complexRegexVal)[1];
                                                                                    val[0]=line.split(complexRegexVal)[0];
                                                                                    val[1]=ref;
                                                                                    return val;
                                                                    }
                                                        }
                                        }
                                        
                                        val[0]=line;
                                        return val;
                            }   
                            /////////////////////////////////////

                             //VALUES FROM TEST METHODS
                             public  String getValueFromTestName(String line){
                                        Pattern pattern = Pattern.compile("[\\d+]([\\.\\d]*)$");
                                        Matcher matcher = pattern.matcher(line);
                                        if(matcher.find()){
                                                        return matcher.group();
                                        }
                                        return null;
                            }
                            /////////////////////////////////////

                             //VALUES FROM REFERENCE RANGES
                            public  String getValueFromReference(String line){
                                        if(line!=null){

                                                        //EXCEPTION - CORTISOL//
                                                        if(line.contains("Post Dexamethasone")){
                                                                        Pattern pattern = Pattern.compile("[\\d+][\\.][\\d\\d]");
                                                                        Matcher matcher = pattern.matcher(line);
                                                                        if(matcher.find()){
                                                                                        return matcher.group();
                                                                        }
                                                        }
                                                        
                                                        //VALUE AT THE BACK//
                                                        Pattern pattern = Pattern.compile("[o|l|L] [\\d+]([\\.\\d]*)$");
                                                        Matcher matcher = pattern.matcher(line);
                                                        if(matcher.find()){
                                                                        return matcher.group().substring(2);
                                                        }
                                                        else{
                                                                        Pattern pattern3 = Pattern.compile("([0-9][.][0-9]+[.][0-9]+)$");
                                                                        Matcher matcher3=pattern3.matcher(line);
                                                                        if(matcher3.find()){
                                                                                    String val=matcher3.group();
                                                                                    String[] splittedVal=val.split("\\.");
                                                                                    Integer w=Integer.parseInt(splittedVal[1])%10;
                                                                                    Integer d=Integer.parseInt(splittedVal[2]);
                                                                                    return w.toString()+"."+d.toString();
                                                                        }
                                                         }

                                                        //VALUE AT THE FRONT//
                                                        if(line.length()>=12){
                                                                        Matcher matcherFrontVal = Pattern.compile("[\\d+]([\\.\\d]*)[A|M|R|m|N|C|D]").matcher(line.substring(0, 12));
                                                                        if(matcherFrontVal!=null){
                                                                                            if(line.length()>=12&&matcherFrontVal.find()){
                                                                                                            String output = matcherFrontVal.group();
                                                                                                            return output.substring(0, output.length()-1);
                                                                                            }
                                                                                            //EXCEPTION - FOR AMH
                                                                                            else if(line.contains("0.96 - 13.3426 - 30 Years")){
                                                                                                    matcherFrontVal = Pattern.compile("[\\d+]([\\.\\d]*)[F]").matcher(line.substring(0, 12));
                                                                                                    if(matcherFrontVal!=null){
                                                                                                                        if(line.length()>=12&&matcherFrontVal.find()){
                                                                                                                                        String output = matcherFrontVal.group();
                                                                                                                                        return output.substring(0, output.length()-1);
                                                                                                                        }
                                                                                                    }
                                                                                            }
                                                                        }
                                                                        
                                                        }
                                                        
                                        }
                             return null;
                             }
                           /////////////////////////////
                           
                            //UNIT UPDATE AND TEST NAME CLEANING
                            public Test doUnitUpdateAndTestNameCleaning(Test testData){
                                return doReferenceBasedUnitUpdate(doTestNameCleaning(doTestNameBasedUnitUpdate(testData)));
                            }
                            //////////////////////////////
                            
                            //REFERENCE BASED RANGE TYPE AND BOUNDARY UPDATE//
                            public Test doReferenceRangeTypeAndBoundaryUpdate(Report report, Test testData){
                                                String reference=testData.getReferenceChunk();
                                                
                                                //CLEANING REFERENCE CHUNK
                                                if(testData.getUnits()!=null&&!testData.getUnits().contains("Nil")){
                                                        reference=reference.replace(testData.getUnits(), "");
                                                }
                                                reference=reference.replace("upto", "<");
                                                reference=reference.replace("up to", "<");
                                                reference=reference.replace("Less than", "<");
                                                reference=reference.replace("to", "-");
                                                
                                                //EXCEPTION - FOR CORTISOL//
                                                reference=reference.split("Post Dexamethasone")[0];
                                                
                                                //EXCEPTION - CV - HEMO GROUP//
                                                Matcher matcher = Pattern.compile("-CV[)]$").matcher(reference);
                                                if(matcher.find()){
                                                        reference=reference.replace(matcher.group(), "");
                                                }
                                                reference=reference.trim();
                                                //EXCEPTION 17 OH PROGESTERONE//
                                                if(testData.getName().startsWith("17 OH PROGESTERONE")){
                                                       testData.getRangeValues().add(new ReferenceRange("FemaleHormoneRange", reference));
                                                        return testData;
                                                }
                                                
                                                
                                                //HORMONAL TESTS//
                                                if(reference.contains("MENSTRUATING")||reference.contains("Menstruating")){
                                                       testData.getRangeValues().add(new ReferenceRange("FemaleHormoneRange", reference));
                                                        return testData;
                                                }
                                                if(reference.contains("Follicular")||reference.contains("FOLLICULAR")){
                                                       testData.getRangeValues().add(new ReferenceRange("FemaleHormoneRange", reference));
                                                        return testData;
                                                }
                                                //TESTOSTERONE TEST//
                                                if(testData.getName().contains("TESTOSTERONE")&&(reference.contains("BOYS")||reference.contains("Boys"))){
                                                    
                                                        testData.getRangeValues().add(new ReferenceRange("MaleHormoneRange", reference));
                                                        
                                                        String admale=reference.split("Adult[( )*]male")[1].split("Adult[( )*]female")[0].replace(":", "").trim();
                                                        String adfemale=reference.split("Adult female")[1].split("Boys[( )*][( )*]<[( )*]1[( )*]year")[0].replace(":", "").trim();
                                                        String boys1=reference.split("Boys[( )*][( )*]<[( )*]1[( )*]year")[1].split("1[( )*]-[( )*]6[( )*]years")[0].replace(":", "").trim();
                                                        String boys2=reference.split("1[( )*]-[( )*]6[( )*]years")[1].split("7[( )*]-[( )*]12[( )*]years")[0].replace(":", "").trim();
                                                        String boys3=reference.split("7[( )*]-[( )*]12[( )*]years")[1].split("13[( )*]-[( )*]17[( )*]years")[0].replace(":", "").trim();
                                                        String boys4=reference.split("13[( )*]-[( )*]17[( )*]years")[1].replace(":", "").trim();
                                                        
                                                       testData.getRangeValues().add(new ReferenceRange("AdultMale", admale));
                                                       testData.getRangeValues().add(new ReferenceRange("AdultFemale", adfemale));
                                                       testData.getRangeValues().add(new ReferenceRange("Boys <1", boys1));
                                                       testData.getRangeValues().add(new ReferenceRange("Boys 1-6", boys2));
                                                       testData.getRangeValues().add(new ReferenceRange("Boys 7-12", boys3));
                                                       testData.getRangeValues().add(new ReferenceRange("Boys 13-17", boys4));
                                                        
                                                        return testData;
                                                }
                                                //PRESENCE TESTS//
                                                if(reference.contains("DEFICIENCY")||reference.contains("Deficiency")){
                                                   
                                                        testData.getRangeValues().add(new ReferenceRange("PresenceRange", reference));
                                                        
                                                          String[] refArray = reference.split(":");
                                                          String def=refArray[1].split("[I|i][N|n][S|s][U|u][F|f][F|f][I|i][C|c][I|i][E|e][N|n][C|c][Y|y]")[0].trim().replace(":", "").trim();
                                                          String insuff=refArray[2].split("[S|s][U|u][F|f][F|f][I|i][C|c][I|i][E|e][N|n][C|c][Y|y]")[0].trim().replace(":", "").trim();
                                                          String suff=refArray[3].split("[T|t][O|o][X|x][I|i][C|c][I|i][T|t][Y|y]")[0].trim().replace(":", "").trim();
                                                          String toxicity=refArray[4].trim().replace(":", "").trim();
                                                          testData.getRangeValues().add(new ReferenceRange("Def", def));
                                                          testData.getRangeValues().add(new ReferenceRange("Ins", insuff));
                                                          testData.getRangeValues().add(new ReferenceRange("Suf", suff));
                                                          testData.getRangeValues().add(new ReferenceRange("Tox", toxicity));
                                                        
                                                        return testData;
                                                }
                                              
                                                
                                                //DIABETIC TESTS//
                                                if(reference.contains("CONTROL")||reference.contains("Control")){
                                                    reference=reference.replace("Unsatisfac-ry", "Unsatisfactory").replace("Below", "<").replace("Above", ">");
                                                    testData.getRangeValues().add(new ReferenceRange("DiabeticRange", reference));
                                                    if(reference.contains("Panic Value")&&reference.contains("Action Suggested")&&reference.contains("Average Control")&&reference.contains("Good Control")&&reference.contains("Excellent Control")){
                                                        String norm=reference.split("Excellent Control")[0].replace(" ", "").replace(":", "").trim();
                                                        String good=reference.split("Excellent Control")[1].split("Good Control")[0].replace(" ", "").replace(":", "").trim();
                                                        String fair=reference.split("Excellent Control")[1].split("Good Control")[1].split("Average Control")[0].replace(" ", "").replace(":", "").trim();
                                                        String unsat=reference.split("Excellent Control")[1].split("Good Control")[1].split("Average Control")[1].split("Action Suggested")[0].replace(" ", "").replace(":", "").trim();
                                                        String poor=reference.split("Excellent Control")[1].split("Good Control")[1].split("Average Control")[1].split("Action Suggested")[1].split("Panic Value")[0].replace(" ", "").replace(":", "").trim();
                                                        testData.getRangeValues().add(new ReferenceRange("Norm", norm));
                                                          testData.getRangeValues().add(new ReferenceRange("Good", good));
                                                          testData.getRangeValues().add(new ReferenceRange("Fair", fair));
                                                          testData.getRangeValues().add(new ReferenceRange("Unsat", unsat));
                                                          testData.getRangeValues().add(new ReferenceRange("Poor", poor));
                                                    
                                                    }
                                                    else if(reference.contains(" Poor Control")&&reference.contains("Unsatisfactory Control")&&reference.contains("Fair Control")&&reference.contains("Good Control")&&reference.contains("Normal Value")){
                                                        String norm=reference.split("Normal Value")[0].replace(" ", "").replace("-", " ").trim().replace(" ", "-").replace(":", "").trim();
                                                        String good=reference.split("Normal Value")[1].split("Good Control")[0].replace(" ", "").replace("-", " ").trim().replace(" ", "-").replace(":", "").trim();
                                                        String fair=reference.split("Normal Value")[1].split("Good Control")[1].split("Fair Control")[0].replace(" ", "").replace("-", " ").trim().replace(" ", "-").replace(":", "").trim();
                                                        String unsat=reference.split("Normal Value")[1].split("Good Control")[1].split("Fair Control")[1].split("Unsatisfactory Control")[0].replace(" ", "").replace("-", " ").trim().replace(" ", "-").replace(":", "").trim();
                                                        String poor=reference.split("Normal Value")[1].split("Good Control")[1].split("Fair Control")[1].split("Unsatisfactory Control")[1].split("Poor Control")[0].replace(" ", "").replace("-", " ").trim().replace(" ", "-").replace(":", "").trim();
                                                        testData.getRangeValues().add(new ReferenceRange("Norm", norm));
                                                          testData.getRangeValues().add(new ReferenceRange("Good", good));
                                                          testData.getRangeValues().add(new ReferenceRange("Fair", fair));
                                                          testData.getRangeValues().add(new ReferenceRange("Unsat", unsat));
                                                          testData.getRangeValues().add(new ReferenceRange("Poor", poor));
                                                    }
                                                        return testData;
                                                }
                                                //NEG EQ POS TESTS//
                                                if(reference.contains("EQUIVOCAL")||reference.contains("Equivocal")){
                                                    
                                                        testData.getRangeValues().add(new ReferenceRange("NegEqPosRange", reference));
                                                        if(reference.split("[E|e][Q|q][U|u][I|i][V|v][O|o][C|c][A|a][L|l]").length==2){
                                                            String negRef=reference.split("[E|e][Q|q][U|u][I|i][V|v][O|o][C|c][A|a][L|l]")[0].replaceAll("[N|n][E|e][G|g][A|a][T|t][I|i][V|v][E|e]", "").trim().replace(":", "");
                                                            String posRef=reference.split("[E|e][Q|q][U|u][I|i][V|v][O|o][C|c][A|a][L|l]")[1].split("[P|p][O|o][S|s][I|i][T|t][I|i][V|v][E|e]")[1].trim().replace(":", "");
                                                            String eqRef=reference.split("[E|e][Q|q][U|u][I|i][V|v][O|o][C|c][A|a][L|l]")[1].split("[P|p][O|o][S|s][I|i][T|t][I|i][V|v][E|e]")[0].trim().replace(":", "");
                                                            if(negRef.contains("<")){
                                                                negRef="<"+negRef.split("<")[1].trim();
                                                            }
                                                            
                                                        testData.getRangeValues().add(new ReferenceRange("Neg", negRef));
                                                        eqRef=eqRef.replace("–", "-");
                                                        testData.getRangeValues().add(new ReferenceRange("Eq", eqRef));
                                                        
                                                        testData.getRangeValues().add(new ReferenceRange("Pos", posRef));
                                                        }
                                                        return testData;
                                                }
                                                //HEART TESTS//
                                                if(reference.contains("RISK")||reference.contains("Risk")){
                                                    if(reference.contains("Disease")){
                                                    reference=reference.split("[H|h][E|e][A|a][R|r][T|t][ ][D|D][I|i][S|s][E|e][A|a][S|s][E|e]")[1].replace(".", "");
                                                    }   
                                                    testData.getRangeValues().add(new ReferenceRange("HeartRange", reference));
                                                       
                                                        if(reference.split("[L|l][O|o][W|w][ ][R|r][I|i][S|s][K|k]").length==2){
                                                            String lowRef=reference.split("[L|l][O|o][W|w][ ][R|r][I|i][S|s][K|k]")[0].replaceAll("-", "").trim().replace(":", "").trim();
                                                            String avgRef=reference.split("[L|l][O|o][W|w][ ][R|r][I|i][S|s][K|k]")[1].split("[A|a][V|v][E|e][R|r][A|a][G|g][E|e][ ][R|r][I|i][S|s][K|k]")[0].replaceAll("-", "").trim().replace(":", "").trim();
                                                            String highRef=reference.split("[L|l][O|o][W|w][ ][R|r][I|i][S|s][K|k]")[1].split("[A|a][V|v][E|e][R|r][A|a][G|g][E|e][ ][R|r][I|i][S|s][K|k]")[1].split("[H|h][I|i][G|g][H|h][ ][R|r][I|i][S|s][K|k]")[0].replaceAll("-", "").trim().replace(":", "").trim();
                                                            
                                                            
                                                        testData.getRangeValues().add(new ReferenceRange("Low", lowRef));
                                                        avgRef=avgRef.replace("–", "-");
                                                        avgRef=avgRef.replace("  ", "-");
                                                        testData.getRangeValues().add(new ReferenceRange("Avg", avgRef));
                                                        
                                                        testData.getRangeValues().add(new ReferenceRange("High", highRef));
                                                        }
                                                        return testData;
                                                }
                                                //BORDER LINE TESTS//
                                                if(reference.contains("BORDER LINE")||reference.contains("Border line")){
                                                    
                                                    testData.getRangeValues().add(new ReferenceRange("BorderlineRef", reference));
                                                    String normalRef=reference.split("[N|n][O|o][R|r][M|m][A|a][L|l]")[1].split("[B|b][O|o][R|r][D|d][E|e][R|r]")[0].replace(":", "").trim();
                                                    String borderRef=reference.split("[B|b][O|o][R|r][D|d][E|e][R|r][ ][L|l][I|i][N|n][E|e]")[1].replace("–", "-").replace(":", "").trim();
                                                    testData.getRangeValues().add(new ReferenceRange("NormalRef", normalRef));
                                                    testData.getRangeValues().add(new ReferenceRange("BorderRef", borderRef));
                                                    return testData;
                                                }
                                                    
                                                //TIME WISE REFERENCE TESTS//
                                                if(testData.getName().contains("CORTISOL")){
                                                    
                                                        testData.getRangeValues().add(new ReferenceRange("TimeRange", reference));
                                                        testData.getRangeValues().add(new ReferenceRange("07.00 - 10.00 A.M.", reference.split("07.00 - 10.00 A.M.")[1].split("04.00 - 08.00 P.M.")[0].replace(":", "").trim()));
                                                        testData.getRangeValues().add(new ReferenceRange("04.00 - 08.00 P.M.", reference.split("04.00 - 08.00 P.M.")[1].replace(":", "").trim()));
                                                        return testData;
                                                } 
                                                //NEG POS TESTS//
                                                if(reference.contains("Negative")||reference.contains("NEGATIVE")){
                                                    testData.getRangeValues().add(new ReferenceRange("NegPosRange", reference));
                                                            String negRef=reference.split("[N|n][E|e][G|g][A|a][T|t][I|i][V|v][E|e]")[1].split("[P|p][O|o][S|s][I|i][T|t][I|i][V|v][E|e]")[0].replace(":", "").trim();
                                                            String posRef=reference.split("[N|n][E|e][G|g][A|a][T|t][I|i][V|v][E|e]")[1].split("[P|p][O|o][S|s][I|i][T|t][I|i][V|v][E|e]")[1].replace(":", "").trim();
                                                           
                                                        testData.getRangeValues().add(new ReferenceRange("Neg", negRef));
                                                        
                                                        testData.getRangeValues().add(new ReferenceRange("Pos", posRef));
                                                        
                                                        return testData;
                                                }
                                                //AGE WISE REFERENCE TESTS//
                                               if(testData.getName().contains("(DHEAS)")&&testData.getName().startsWith("DHEA")){
                                                    
                                                    testData.getRangeValues().add(new ReferenceRange("AgeRange", reference));
                                                    String[] values=reference.replace(" - ", "-").replace("*", "").split("(   )+");
                                                    System.out.println(values);
                                                     testData.getRangeValues().add(new ReferenceRange("Female 10-14", values[2]));
                                                     testData.getRangeValues().add(new ReferenceRange("Male 10-14", values[3].replace("15-19", "")));
                                                      testData.getRangeValues().add(new ReferenceRange("Female 15-19", values[4]));
                                                     testData.getRangeValues().add(new ReferenceRange("Male 15-19", values[5].replace("20-24", "")));
                                                      testData.getRangeValues().add(new ReferenceRange("Female 20-24", values[6]));
                                                     testData.getRangeValues().add(new ReferenceRange("Male 20-24", values[7].replace("25-34", "")));
                                                      testData.getRangeValues().add(new ReferenceRange("Female 25-34", values[9]));
                                                     testData.getRangeValues().add(new ReferenceRange("Male 25-34", values[10].replace("35-44", "")));
                                                      testData.getRangeValues().add(new ReferenceRange("Female 35-44", values[11]));
                                                     testData.getRangeValues().add(new ReferenceRange("Male 35-44", values[12].replace("45-54", "")));
                                                      testData.getRangeValues().add(new ReferenceRange("Female 45-54", values[13]));
                                                     testData.getRangeValues().add(new ReferenceRange("Male 45-54", values[14].replace("55-64", "")));
                                                      testData.getRangeValues().add(new ReferenceRange("Female 55-64", values[15]));
                                                     testData.getRangeValues().add(new ReferenceRange("Male 55-64", values[16]));
                                                     
                                                     
                                                     
                                                        return testData;
                                                }    
                                                //EXCEPTIONS - ANTI MULLERIAN HORMONE - ONE VALUE REFERENCE
                                                if(testData.getName().contains("ANTI MULLERIAN HORMONE")){
                                                 testData.getRangeValues().add(new ReferenceRange("AgeRange", reference));
                                                     String female1=reference.split("[( )*]18[( )*]-[( )*]25[( )*]Years")[1].split("26[( )*]-[( )*]30[( )*]Years")[0].replace(":", "").trim();
                                                        String female2=reference.split("26[( )*]-[( )*]30[( )*]Years")[1].split("31[( )*]-[( )*]35[( )*]Years")[0].replace(":", "").trim();
                                                        String female3=reference.split("31[( )*]-[( )*]35[( )*]Years")[1].split("36[( )*]-[( )*]40[( )*]Years")[0].replace(":", "").trim();
                                                        String female4=reference.split("36[( )*]-[( )*]40[( )*]Years")[1].split("41[( )*]-[( )*]45[( )*]Years")[0].replace(":", "").trim();
                                                        String female5=reference.split("41[( )*]-[( )*]45[( )*]Years")[1].split(">[( )*]45[( )*]Years")[0].replace(":", "").trim();
                                                      String male=reference.split(">[( )*]45[( )*]Years")[1].split("Male")[0].replace(":", "").trim();
                                                      
                                                      testData.getRangeValues().add(new ReferenceRange("Male", male));
                                                       testData.getRangeValues().add(new ReferenceRange("Female 18-25", female1));
                                                       testData.getRangeValues().add(new ReferenceRange("Female 26-30", female2));
                                                       testData.getRangeValues().add(new ReferenceRange("Female 31-35", female3));
                                                       testData.getRangeValues().add(new ReferenceRange("Female 36-40", female4));
                                                       testData.getRangeValues().add(new ReferenceRange("Female >45", female5));
                                                    
                                                    return testData;
                                                }
                                                //EXCEPTION - NORMAL TEST//
                                                if(reference.contains("Normal")||reference.contains("NORMAL")){
                                                     
                                                    String[] refArray=reference.split(":");
                                                    reference=refArray[refArray.length-1];
                                                    if(reference.substring(reference.length()-1, reference.length()).equals("-")||reference.substring(reference.length()-1, reference.length()).equals(";")){
                                                        reference=reference.substring(0, reference.length()-1).trim();
                                                    }
                                                    
                                                    testData.getRangeValues().add(new ReferenceRange("Range", reference));
                                                    
                                                        return testData;
                                                }
                                                
                                                //ONE WORD REFERENCE//
                                                if(reference.contains("Nil in adults")){
                                                            testData.getRangeValues().add(new ReferenceRange("NilRange","Nil"));
                                                            return testData;
                                                }
                                                
                                                //EXCEPTIONS - IMMATURE GRANULOCYTES(IG) - ONE VALUE REFERENCE
                                                if(testData.getName().equals("IMMATURE GRANULOCYTES(IG)")){
                                                    testData.getRangeValues().add(new ReferenceRange("Exact", "0.03"));
                                                    return testData;
                                                }
                                                //MALE FEMALE BIFURCATION//
                                                String[] maleFemaleDiff = reference.split("Female[s]*");
                                                if(maleFemaleDiff.length>1){
                                                                    if(maleFemaleDiff[0].split("Male").length>1){
                                                                                            reference=maleFemaleDiff[0].split("Male[s]*")[1].trim();
                                                                                            String[] refArray=reference.split(":");
                                                                                            reference=refArray[refArray.length-1];
                                                                                            if(reference.substring(reference.length()-1, reference.length()).equals("-")||reference.substring(reference.length()-1, reference.length()).equals(";")||reference.substring(reference.length()-1, reference.length()).equals(".")||reference.substring(reference.length()-1, reference.length()).equals(",")){
                                                                                                        reference=reference.substring(0, reference.length()-1).trim();
                                                                                            }
                                                                                            if(reference.split("-").length==2){
                                                                                                        reference=reference.split("-")[0].trim()+"-"+reference.split("-")[1].trim().split(" ")[0].trim();
                                                                                            }
                                                                                            testData.getRangeValues().add(new ReferenceRange("Male",reference));
                                                                                            reference=maleFemaleDiff[1].trim();
                                                                                            refArray=reference.split(":");
                                                                                            reference=refArray[refArray.length-1];
                                                                                            if(reference.substring(reference.length()-1, reference.length()).equals("-")||reference.substring(reference.length()-1, reference.length()).equals(";")||reference.substring(reference.length()-1, reference.length()).equals(".")||reference.substring(reference.length()-1, reference.length()).equals(",")){
                                                                                                        reference=reference.substring(0, reference.length()-1).trim();
                                                                                            }
                                                                                            if(reference.split("-").length==2){
                                                                                                        reference=reference.split("-")[0].trim()+"-"+reference.split("-")[1].trim().split(" ")[0].trim();
                                                                                            }
                                                                                            testData.getRangeValues().add(new ReferenceRange("Female",reference ));
                                                                                            return testData;
                                                                    }
                                                                    
                                                }
                                                maleFemaleDiff = reference.split("F:");
                                                if(maleFemaleDiff.length>1){
                                                                    if(maleFemaleDiff[0].split("M:").length>1){
                                                                                            reference=maleFemaleDiff[0].split("M")[1].trim();
                                                                                            String[] refArray=reference.split(":");
                                                                                            reference=refArray[refArray.length-1];
                                                                                            if(reference.substring(reference.length()-1, reference.length()).equals("-")||reference.substring(reference.length()-1, reference.length()).equals(";")||reference.substring(reference.length()-1, reference.length()).equals(".")||reference.substring(reference.length()-1, reference.length()).equals(",")){
                                                                                                            reference=reference.substring(0, reference.length()-1).trim();
                                                                                            }
                                                                                            if(reference.split("-").length==2){
                                                                                                            reference=reference.split("-")[0].trim()+"-"+reference.split("-")[1].trim().split(" ")[0].trim();
                                                                                            }
                                                                                            testData.getRangeValues().add(new ReferenceRange("Male",reference));
                                                                                             reference=maleFemaleDiff[1].trim();
                                                                                            refArray=reference.split(":");
                                                                                            reference=refArray[refArray.length-1];
                                                                                            if(reference.substring(reference.length()-1, reference.length()).equals("-")||reference.substring(reference.length()-1, reference.length()).equals(";")||reference.substring(reference.length()-1, reference.length()).equals(".")||reference.substring(reference.length()-1, reference.length()).equals(",")){
                                                                                                            reference=reference.substring(0, reference.length()-1).trim();
                                                                                            }
                                                                                            reference=reference.split("-")[0].trim()+"-"+reference.split("-")[1].trim().split(" ")[0].trim();
                                                                                            testData.getRangeValues().add(new ReferenceRange("Female", reference));
                                                                                            return testData;
                                                                    }
                                                }
                                                maleFemaleDiff = reference.split("F :");
                                                if(maleFemaleDiff.length>1){
                                                                    if(maleFemaleDiff[0].split("M:").length>1){
                                                                                            reference=maleFemaleDiff[0].split("M")[1].trim();
                                                                                            String[] refArray=reference.split(":");
                                                                                            reference=refArray[refArray.length-1];
                                                                                            if(reference.substring(reference.length()-1, reference.length()).equals("-")||reference.substring(reference.length()-1, reference.length()).equals(";")||reference.substring(reference.length()-1, reference.length()).equals(".")||reference.substring(reference.length()-1, reference.length()).equals(",")){
                                                                                                reference=reference.substring(0, reference.length()-1).trim();
                                                                                            }
                                                                                            if(reference.split("-").length==2){
                                                                                                        reference=reference.split("-")[0].trim()+"-"+reference.split("-")[1].trim().split(" ")[0].trim();
                                                                                            }
                                                                                            testData.getRangeValues().add(new ReferenceRange("Male",reference));
                                                                                             reference=maleFemaleDiff[1].trim();
                                                                                            refArray=reference.split(":");
                                                                                            reference=refArray[refArray.length-1];
                                                                                            if(reference.substring(reference.length()-1, reference.length()).equals("-")||reference.substring(reference.length()-1, reference.length()).equals(";")||reference.substring(reference.length()-1, reference.length()).equals(".")||reference.substring(reference.length()-1, reference.length()).equals(",")){
                                                                                                reference=reference.substring(0, reference.length()-1).trim();
                                                                                            }
                                                                                            if(reference.split("-").length==2){
                                                                                                        reference=reference.split("-")[0].trim()+"-"+reference.split("-")[1].trim().split(" ")[0].trim();
                                                                                            }
                                                                                            testData.getRangeValues().add(new ReferenceRange("Female", reference));
                                                                                            return testData;
                                                                    }
                                                }
                                                maleFemaleDiff = reference.split("Women");
                                                if(maleFemaleDiff.length>1){
                                                                    if(maleFemaleDiff[0].split("Men").length>1){
                                                                                            reference=maleFemaleDiff[0].split("Men")[1].trim();
                                                                                            String[] refArray=reference.split(":");
                                                                                            reference=refArray[refArray.length-1];
                                                                                            if(reference.substring(reference.length()-1, reference.length()).equals("-")||reference.substring(reference.length()-1, reference.length()).equals(";")||reference.substring(reference.length()-1, reference.length()).equals(".")||reference.substring(reference.length()-1, reference.length()).equals(",")){
                                                                                                reference=reference.substring(0, reference.length()-1).trim();
                                                                                            }
                                                                                            if(reference.split("-").length==2){
                                                                                                        reference=reference.split("-")[0].trim()+"-"+reference.split("-")[1].trim().split(" ")[0].trim();
                                                                                            }
                                                                                            testData.getRangeValues().add(new ReferenceRange("Male",reference));
                                                                                            reference=maleFemaleDiff[1].trim();
                                                                                            refArray=reference.split(":");
                                                                                            reference=refArray[refArray.length-1];
                                                                                            if(reference.substring(reference.length()-1, reference.length()).equals("-")||reference.substring(reference.length()-1, reference.length()).equals(";")||reference.substring(reference.length()-1, reference.length()).equals(".")||reference.substring(reference.length()-1, reference.length()).equals(",")){
                                                                                                reference=reference.substring(0, reference.length()-1).trim();
                                                                                            }
                                                                                            if(reference.split("-").length==2){
                                                                                                          reference=reference.split("-")[0].trim()+"-"+reference.split("-")[1].trim().split(" ")[0].trim();
                                                                                            }
                                                                                            testData.getRangeValues().add(new ReferenceRange("Female", reference));
                                                                                            return testData;
                                                                    }
                                                }
                                                
                                                
                                                if(reference.contains("Adult")||!Pattern.compile("[a-zA-Z]+").matcher(reference).find()){
                                                                        //EXCEPTIONS THYRO PROFILE//
                                                                        if(testData.getName().startsWith("THYROID STIMULATING HORMONE")){
                                                                                        String[] refArray=reference.split("5.5");
                                                                                        testData.getRangeValues().add(new ReferenceRange("Range",refArray[0]+"5.5"));
                                                                                        if(refArray.length==2){
                                                                                                       testData.setValue(refArray[1]);
                                                                                        }
                                                                                        return testData;
                                                                                    
                                                                        }
                                                                        else if(testData.getName().startsWith("FREE TRIIODOTHYRONINE")){
                                                                                        String[] refArray=reference.split("4.2");
                                                                                        testData.getRangeValues().add(new ReferenceRange("Range",refArray[0]+"4.2"));
                                                                                        if(refArray.length==2){
                                                                                                        testData.setValue(refArray[1]);
                                                                                        }
                                                                                        return testData;
                                                                        }
                                                                        else if(testData.getName().startsWith("FREE THYROXINE")){
                                                                                        String[] refArray=reference.split("1.80");
                                                                                         testData.getRangeValues().add(new ReferenceRange("Range",refArray[0]+"1.80"));
                                                                                        if(refArray.length==2){
                                                                                                        testData.setValue(refArray[1]);
                                                                                        }
                                                                                        return testData;
                                                                        }
                                                    
                                                    
                                                    
                                                                        //FROM TO REFERENCE//
                                                                        matcher = Pattern.compile("[0-9]+([,.][0-9]+)*[( )*]-[( )*][0-9]+([,.][0-9]+)*").matcher(reference);
                                                                        if(matcher.find()){
                                                                                    reference=matcher.group().trim();
                                                                                    String[] refArray=reference.split(":");
                                                                                    reference=refArray[refArray.length-1];
                                                                                    if(reference.substring(reference.length()-1, reference.length()).equals("-")||reference.substring(reference.length()-1, reference.length()).equals(";")){
                                                                                        reference=reference.substring(0, reference.length()-1).trim();
                                                                                    }
                                                                                    
                                                    
                                                                                    testData.getRangeValues().add(new ReferenceRange("Range",reference));
                                                                                    return testData; 
                                                                        }
                                                                        matcher = Pattern.compile("[0-9]+([,.][0-9]+)*-[0-9]+([,.][0-9]+)*").matcher(reference);
                                                                        if(matcher.find()){
                                                                                    reference=matcher.group().trim();
                                                                                    String[] refArray=reference.split(":");
                                                                                    reference=refArray[refArray.length-1];
                                                                                    if(reference.substring(reference.length()-1, reference.length()).equals("-")||reference.substring(reference.length()-1, reference.length()).equals(";")){
                                                                                        reference=reference.substring(0, reference.length()-1).trim();
                                                                                    }
                                                                                    
                                                    
                                                                                    testData.getRangeValues().add(new ReferenceRange("Range",reference));
                                                                                    return testData; 
                                                                        }
                                                                         matcher = Pattern.compile("[0-9]+([,.][0-9]+)*  -  [0-9]+([,.][0-9]+)*").matcher(reference);
                                                                        if(matcher.find()){
                                                                                    reference=matcher.group().trim();
                                                                                    String[] refArray=reference.split(":");
                                                                                    reference=refArray[refArray.length-1];
                                                                                    if(reference.substring(reference.length()-1, reference.length()).equals("-")||reference.substring(reference.length()-1, reference.length()).equals(";")){
                                                                                        reference=reference.substring(0, reference.length()-1).trim();
                                                                                    }
                                                    
                                                                                    testData.getRangeValues().add(new ReferenceRange("Range",reference));
                                                                                    return testData; 
                                                                        }
                                                                         matcher = Pattern.compile("[0-9]+([,.][0-9]+)* -  [0-9]+([,.][0-9]+)*").matcher(reference);
                                                                        if(matcher.find()){
                                                                                    reference=matcher.group().trim();
                                                                                    String[] refArray=reference.split(":");
                                                                                    reference=refArray[refArray.length-1];
                                                                                    if(reference.substring(reference.length()-1, reference.length()).equals("-")||reference.substring(reference.length()-1, reference.length()).equals(";")){
                                                                                        reference=reference.substring(0, reference.length()-1).trim();
                                                                                    }
                                                    
                                                                                    testData.getRangeValues().add(new ReferenceRange("Range",reference));
                                                                                    return testData; 
                                                                        }
                                                
                                                
                                                                        //LESS THAN REFERENCE//
                                                                        matcher = Pattern.compile("< [0-9]+([,.][0-9]+)*").matcher(reference);
                                                                        if(matcher.find()){
                                                                                    reference=matcher.group().trim();
                                                                                    String[] refArray=reference.split(":");
                                                                                    reference=refArray[refArray.length-1];
                                                                                    if(reference.substring(reference.length()-1, reference.length()).equals("-")||reference.substring(reference.length()-1, reference.length()).equals(";")){
                                                                                        reference=reference.substring(0, reference.length()-1).trim();
                                                                                    }
                                                    
                                                                                    testData.getRangeValues().add(new ReferenceRange("LessRange",reference));
                                                                                    return testData; 
                                                                        }
                                                                        matcher = Pattern.compile("<  [0-9]+([,.][0-9]+)*").matcher(reference);
                                                                        if(matcher.find()){
                                                                                    reference=matcher.group().trim();
                                                                                    String[] refArray=reference.split(":");
                                                                                    reference=refArray[refArray.length-1];
                                                                                    if(reference.substring(reference.length()-1, reference.length()).equals("-")||reference.substring(reference.length()-1, reference.length()).equals(";")){
                                                                                        reference=reference.substring(0, reference.length()-1).trim();
                                                                                    }
                                                    
                                                                                    testData.getRangeValues().add(new ReferenceRange("LessRange",reference));
                                                                                    return testData; 
                                                                        }
                                                
                                                
                                                                        //MORE THAN REFERENCE//
                                                                        if(reference.contains("Adult")||!Pattern.compile("[a-zA-Z]+").matcher(reference).find()){
                                                                                    matcher = Pattern.compile("> [0-9]+([,.][0-9]+)*").matcher(reference);
                                                                                    if(matcher.find()){
                                                                                                reference=matcher.group().trim();
                                                                                                String[] refArray=reference.split(":");
                                                                                                reference=refArray[refArray.length-1];
                                                                                                if(reference.substring(reference.length()-1, reference.length()).equals("-")||reference.substring(reference.length()-1, reference.length()).equals(";")){
                                                                                                    reference=reference.substring(0, reference.length()-1).trim();
                                                                                                }
                                                    
                                                                                                testData.getRangeValues().add(new ReferenceRange("MoreRange",reference));
                                                                                                return testData; 
                                                                                    }
                                                                                    matcher = Pattern.compile(">  [0-9]+([,.][0-9]+)*").matcher(reference);
                                                                                    if(matcher.find()){
                                                                                                reference=matcher.group().trim();
                                                                                                String[] refArray=reference.split(":");
                                                                                                reference=refArray[refArray.length-1];
                                                                                                if(reference.substring(reference.length()-1, reference.length()).equals("-")||reference.substring(reference.length()-1, reference.length()).equals(";")){
                                                                                                    reference=reference.substring(0, reference.length()-1).trim();
                                                                                                }
                                                    
                                                                                                testData.getRangeValues().add(new ReferenceRange("MoreRange",reference));
                                                                                                return testData; 
                                                                                    }
                                                                        }
                                                }
                                                
                                               
                                                
                                                System.err.println("Reference Range not updated");
                                                return testData;
                            }
                            /////////////////////////
                            
                            //PROFILING AND ADDING TEST DATA
                            public Report addAndProfileData(Report report,Test testData){
                                if(testData.getName().contains("ANTI CCP")||testData.getName().contains("ANTI NUCLEAR")||testData.getName().contains("ANTI STREPTOLYSIN")||
                                      testData.getName().contains("COMPLEMENT 4")||testData.getName().contains("PHOSPHOROUS")||testData.getName().contains("RHEUMATOID FACTOR")||testData.getName().contains("HLA-B")  ){
                                    
                                    
                                    profileArthritis.getTestData().add(testData);
                                    testData.setProfile(profileArthritis);
                                    
                                    if(!flagArthritis){
                                            report.getProfileData().add(profileArthritis);
                                            profileArthritis.setReport(report);
                                            flagArthritis=true;
                                    }
                                    return report;
                                    
                                }
                                else if(testData.getName().contains("APO B / APO A")||
                                        testData.getName().contains("HOMOCYSTEINE")||testData.getName().contains("HIGH SENSITIVITY C-REACTIVE PROTEIN")||testData.getName().contains("Lipoprotein")||
                                        testData.getName().contains("C-REACTIVE PROTEIN")||testData.getName().contains("LIPOPROTEIN")){
                                        
                                    
                                    profileHeart.getTestData().add(testData);
                                    testData.setProfile(profileHeart);
                                    if(!flagHeart){
                                        report.getProfileData().add(profileHeart);
                                        profileHeart.setReport(report);
                                        flagHeart=true;
                                    }
                                    return report;
                                }
                                 else if(testData.getName().contains("LYMPHOCYTE")||testData.getName().contains("MONOCYTE")||testData.getName().contains("NEUTROPHIL")||testData.getName().contains("BASOPHIL")||
                                         testData.getName().contains("EOSINOPHIL")||testData.getName().contains("LEUCOCYTE")||testData.getName().contains("IMMATURE GRANULOCYTE")||testData.getName().contains("HEMOGLOBIN")||
                                         testData.getName().contains("PLATELET")||testData.getName().contains("NUCLEATED RED BLOOD CELLS")||testData.getName().contains("CONC(MCHC)")||testData.getName().contains("MEAN CORPUSCULAR")||
                                         testData.getName().contains("HEMATOCRIT")||testData.getName().contains("TOTAL RBC")||testData.getName().contains("RED CELL DISTRIBUTION")){
                                   
                                    
                                    profileBlood.getTestData().add(testData);
                                    testData.setProfile(profileBlood);
                                    if(!flagBlood){
                                            report.getProfileData().add(profileBlood);
                                            profileBlood.setReport(report);
                                            flagBlood=true;
                                    }
                                    return report;
                                }
                                 else if(testData.getName().contains("AVERAGE BLOOD GLUCOSE")||testData.getName().contains("HbA1c")){
                                     
                                    
                                    profileDiabetes.getTestData().add(testData);
                                    testData.setProfile(profileDiabetes);
                                    
                                    if(!flagDiabetes){
                                            report.getProfileData().add(profileDiabetes);
                                            profileDiabetes.setReport(report);
                                            flagDiabetes=true;
                                    }
                                    return report;

                                }
                                 else if(testData.getName().contains("CHLORIDE")||testData.getName().contains("SODIUM")){
                                    
                                    
                                    profileElectrolytes.getTestData().add(testData);
                                    testData.setProfile(profileElectrolytes);
                                    
                                   if(!flagElectrolytes){
                                            report.getProfileData().add(profileElectrolytes);
                                            profileElectrolytes.setReport(report);
                                            flagElectrolytes=true;
                                    }
                                    return report;

                                }
                                 else if(testData.getName().contains("IRON")||testData.getName().contains("TRANSFERRIN SATURATION")||
                                         testData.getName().contains("FERRITIN")){
                                         
                                    
                                    profileIron.getTestData().add(testData);
                                    testData.setProfile(profileIron);
                                    
                                    if(!flagIron){
                                            report.getProfileData().add(profileIron);
                                            profileIron.setReport(report);
                                            flagIron=true;
                                    }
                                    return report;
                                }
                                 else if(testData.getName().contains("CHOLESTEROL")||testData.getName().contains("LDL / HDL RATIO")||
                                         testData.getName().contains("TRIGLYCERIDES")){
                                     
                                    
                                    profileLipid.getTestData().add(testData);
                                    testData.setProfile(profileLipid);
                                    
                                   if(!flagLipid){
                                            report.getProfileData().add(profileLipid);
                                            profileLipid.setReport(report);
                                            flagLipid=true;
                                    }
                                    return report;
                                }
                                 else if(testData.getName().contains("ALANINE TRANSAMINASE ")||testData.getName().contains("ALKALINE PHOSPHATASE")||testData.getName().contains("BILIRUBIN")||
                                         testData.getName().contains("GAMMA GLUTAMYL")||testData.getName().contains("PROTEIN - TOTAL")||testData.getName().contains("ALBUMIN - SERUM")||
                                         testData.getName().contains("GLOBULIN")||testData.getName().contains("ASPARTATE AMINOTRANSFERASE")){
                                   
                                    
                                    profileLiver.getTestData().add(testData);
                                    testData.setProfile(profileLiver);
                                    
                                    if(!flagLiver){
                                            report.getProfileData().add(profileLiver);
                                            profileLiver.setReport(report);
                                            flagLiver=true;
                                    }
                                    return report;
                                }
                                 else if(testData.getName().contains("CREATININE")||testData.getName().contains("BLOOD UREA NITROGEN")||
                                         testData.getName().contains("CALCIUM")||testData.getName().contains("URINARY MICROALBUMIN")||
                                         testData.getName().contains("URIC ACID")||testData.getName().contains("BUN")){
                                    
                                    
                                    profileKidney.getTestData().add(testData);
                                    testData.setProfile(profileKidney);
                                    
                                    if(!flagKidney){
                                            report.getProfileData().add(profileKidney);
                                            profileKidney.setReport(report);
                                            flagKidney=true;
                                    }
                                    return report;


   
                                }
                                 else if(testData.getName().contains("THYROID")||testData.getName().contains("TRIIODOTHYRONINE")||
                                         testData.getName().contains("THYROXINE")){
                               
                                    
                                    profileThyroid.getTestData().add(testData);
                                    testData.setProfile(profileThyroid);
                                    
                                    if(!flagThyroid){
                                            report.getProfileData().add(profileThyroid);
                                            profileThyroid.setReport(report);
                                            flagThyroid=true;
                                    }
                                    return report;


                                }
                                 else if(testData.getName().contains("LEAD")||testData.getName().contains("MERCURY")||testData.getName().contains("MERCURY")||
                                         testData.getName().contains("CAESIUM")||testData.getName().contains("CHROMIUM")||testData.getName().contains("COBALT")||
                                         testData.getName().contains("CADMIUM")||testData.getName().contains("BARIUM")||testData.getName().contains("ARSENIC")||testData.getName().contains("SELENIUM")){
                                    
                                     
                                    
                                    profileToxic.getTestData().add(testData);
                                    testData.setProfile(profileToxic);
                                    
                                    if(!flagToxic){
                                            report.getProfileData().add(profileToxic);
                                            profileToxic.setReport(report);
                                            flagToxic=true;
                                    }
                                    return report;
                                }
                                 else if(testData.getName().contains("FOLIC ACID")||testData.getName().contains("VITAMIN")){
                                    
                                    
                                    profileVitamins.getTestData().add(testData);
                                    testData.setProfile(profileVitamins);
                                    
                                    if(!flagVitamins){
                                            report.getProfileData().add(profileVitamins);
                                            profileVitamins.setReport(report);
                                            flagVitamins=true;
                                    }
                                    return report;
                  
                                }
                                 else if(testData.getName().contains("AMYLASE")||testData.getName().contains("LIPASE")){
                                   
                                    
                                    profilePancreas.getTestData().add(testData);
                                    testData.setProfile(profilePancreas);
                                    
                                   if(!flagPancreas){
                                            report.getProfileData().add(profilePancreas);
                                            profilePancreas.setReport(report);
                                            flagPancreas=true;
                                    }
                                    return report;
                                }
                                 else if(testData.getName().contains("TESTOSTERONE")||testData.getName().contains("HORMONE")||
                                         testData.getName().contains("PROLACTIN")||testData.getName().contains("OESTROGEN")||
                                         testData.getName().contains("PROGESTERONE")||testData.getName().contains("DHEA - SULPHATE")){
                                    
                                    
                                    profileHormones.getTestData().add(testData);
                                    testData.setProfile(profileHormones);
                                    
                                     if(!flagHormones){
                                            report.getProfileData().add(profileHormones);
                                            profileHormones.setReport(report);
                                            flagHormones=true;
                                    }
                                }
                                 else if(testData.getName().contains("PROSTATE")){
                              
                                    profileProstate.getTestData().add(testData);
                                    testData.setProfile(profileProstate);
                                    if(!flagProstate){
                                        report.getProfileData().add(profileProstate);
                                        profileProstate.setReport(report);
                                        flagProstate=true;
                                    }
                                    return report;
                                }
                                 else if(testData.getName().contains("CORTISOL")){
                                     
                                    
                                    profileStress.getTestData().add(testData);
                                    testData.setProfile(profileStress);
                                    
                                    if(!flagStress){
                                        report.getProfileData().add(profileStress);
                                        profileStress.setReport(report);
                                        flagStress=true;
                                    }
                                    return report;
                                }
                                 else if(testData.getName().contains("HEPATITIS")){
                                    
                                    
                                    profileHepatitis.getTestData().add(testData);
                                    testData.setProfile(profileHepatitis);
                                    
                                    if(!flagHepatitis){ 
                                            report.getProfileData().add(profileHepatitis);
                                            profileHepatitis.setReport(report);
                                            flagHepatitis=true;
                                    }
                                    return report;
                                }
                                 //JUNK////////////////////////////////
                                 else if(testData.getName().startsWith("LDL/ - Derived")||testData.getName().startsWith("URINE")||testData.getName().isEmpty()){
                                     
                                 }
                                 ///////////////////////////////////////
                                 else{
                                     
                                     System.err.println("NO CATEGORY FOUND------ERRRROR------ ADD IT TO A CATEGORY "+testData.getName());
                                 }
                                
                                return null;
                            }
                            ///////////////////////////////
                            
                            //TURN ALL PROFILE FLAGS OFF
                            public void turnOffProfileFlags(){
                                flagArthritis=false;
                                flagBlood=false;
                                flagDiabetes=false;
                                flagElectrolytes=false;
                                flagHeart=false;
                                flagHepatitis=false;
                                flagHormones=false;
                                flagIron=false;
                                flagKidney=false;
                                flagLipid=false;
                                flagLiver=false;
                                flagVitamins=false;
                                flagPancreas=false;
                                flagProstate=false;
                                flagStress=false;
                                flagThyroid=false;
                                flagToxic=false;
                                 profileArthritis =new Profile("ARTHRITIS");
                           profileHeart =new Profile("HEART RISKY MARKERS");
                           profileBlood =new Profile("COMPLETE BLOOD COUNT");
                           profileDiabetes =new Profile("DIABETES");
                          profileElectrolytes =new Profile("ELECTROLYTES");
                           profileIron =new Profile("IRON");
                           profileLipid =new Profile("CHOLESTEROL");
                            profileLiver =new Profile("LIVER");
                             profileKidney =new Profile("KIDNEY");
                            profileThyroid =new Profile("THYROID");
                           profileToxic =new Profile("TOXIC ELEMENTS");
                            profileVitamins =new Profile("VITAMINS");
                            profilePancreas =new Profile("PANCREAS");
                            profileHormones =new Profile("HORMONES");
                            profileProstate =new Profile("PROSTATE");
                            profileStress =new Profile("STRESS");
                             profileHepatitis =new Profile("HEPATITIS");
                            }
                            ///////////////////////////////////////////////////////
                            
                            //TEST NAME BASED UNIT UPDATE
                            private Test doTestNameBasedUnitUpdate(Test testData) {
                                                
                                                String units=null;
                                                Matcher matcher=null;
                                                String[] regexExceptionsThyroGroup={"ng/dl", "µIU/ml", "µg/dl","pg/ml"};
                                                
                                                 //NORMAL UNITS WITH TEST NAME UPDATE//
                                                 matcher=Pattern.compile("[a-zA-Z|µ]+[/][a-zA-Z|µ]+[//s]*$").matcher(testData.getName());
                                                if(matcher.find()){
                                                                 units=matcher.group();
                                                                 testData.setUnits(units.trim());
                                                                 testData.setName(testData.getName().replace(units, ""));
                                                                 return testData;
                                                }
                                                
                                                //EXCEPTIONS - THYRO GROUP//
                                                for(String regexExceptionsThyro:regexExceptionsThyroGroup){
                                                                 matcher=Pattern.compile(regexExceptionsThyro).matcher(testData.getName());
                                                                if(matcher.find()){
                                                                                 units=matcher.group();
                                                                                 testData.setUnits(units.trim());
                                                                                 testData.setName(testData.getName().replace(units, ""));
                                                                                 return testData;
                                                                }
                                                }
                                                //EXCEPTION - hs-CRP//
                                                matcher=Pattern.compile("mg/L").matcher(testData.getName());
                                                if(matcher.find()){
                                                                 units=matcher.group();
                                                                 testData.setUnits(units.trim());
                                                                 testData.setName(testData.getName().replace(units, ""));
                                                                 return testData;
                                                }
                                                
                                                //EXCEPTION - RF//
                                                matcher=Pattern.compile("IU/ml<").matcher(testData.getName());
                                                if(matcher.find()){
                                                                 units=matcher.group();
                                                                 testData.setUnits(units.replace("<","").trim());
                                                                 testData.setName(testData.getName().replace(units, ""));
                                                                 return testData;
                                                }

                                                //UNITS IN PERCENTAGE//
                                                matcher=Pattern.compile("[%]$").matcher(testData.getName());
                                                if(matcher.find()){

                                                                 units=matcher.group();
                                                                 testData.setUnits(units.trim());
                                                                 testData.setName(testData.getName().replace(units, ""));
                                                                 return testData;
                                                }
                                                matcher=Pattern.compile("PERCENTAGE").matcher(testData.getName());
                                                if(matcher.find()){

                                                                 units=matcher.group();
                                                                 testData.setUnits(units.trim());
                                                                 testData.setName(testData.getName().replace(units, ""));
                                                                 return testData;
                                                }
                                                
                                                //UNITS IN RATIOS//
                                                matcher=Pattern.compile("Ratio").matcher(testData.getName());
                                                if(matcher.find()){
                                                                 units=matcher.group();
                                                                 testData.setUnits(units.trim());
                                                                 testData.setName(testData.getName().replace(units, ""));
                                                                 return testData;
                                                }
                                                return testData;
                            }
                            //////////////////////////////// 
                            
                            //TEST NAME CLEANING
                            private Test doTestNameCleaning(Test testData) {
                                                
                                                Matcher matcher=null;
                                                String[] regexUnwantedTechnologyNames={"[A-Z]+METRY","[A-Z]([.][A-Z])+","ICP-MS","LC-MS/MS"};
                                                
                                                 testData.setName(testData.getName().trim());
                                                 
                                                 //EXCEPTION - REMOVE UNWANTED VALUES - BUN / SR.CREATININE RATIO CALCULATED//
                                                matcher=Pattern.compile("9:").matcher(testData.getName());
                                                if(matcher.find()){
                                                                 testData.setName(testData.getName().replace(matcher.group(),""));
                                                }
                                                
                                                //REMOVE UNWANTED TECHNOLOGY NAMES//
                                                for (String regexUnwantedTechnologyName : regexUnwantedTechnologyNames) {
                                                    matcher = Pattern.compile(regexUnwantedTechnologyName).matcher(testData.getName());
                                                    if(matcher.find()){
                                                        testData.setName(testData.getName().replace(matcher.group(),""));
                                                    }
                                                }
                                                
                                                return testData;
                            }
                            /////////////////////
                            
                            //REFERENCE BASED UNIT UPDATE
                            private Test doReferenceBasedUnitUpdate(Test testData){
                                
                                String[] regexUnits ={"%","Ratio","X 10³ / µL","X 10[,^]6/µL","pq","fL"};
                                String[] regexAfterTerms={"INSUFFICIENCY","Insufficiency","FEMALE","FEMALE","Female","WOMEN","Women","BORDER","Border","POSITIVE","Positive","NEGATIVE","Negative","F:","F :"};

                                if(testData.getUnits()==null&&testData.getReferenceChunk()!=null){
                                                String units=null;
                                                Matcher matcher=Pattern.compile("[a-zA-Z|µ]+[/][a-zA-Z|µ]+").matcher(testData.getReferenceChunk());
                                                if(matcher.find()){
                                                                 units=matcher.group();
                                                                for (String regexAfterTerm : regexAfterTerms) {
                                                                    units = units.replace(regexAfterTerm, "");
                                                                }
                                                                
                                                                //EXCEPTIONS - FOR ELEMENT DETECTIONS//
                                                                units=units.replace("µg/lµg", "µg/l");
                                                                 testData.setUnits(units.trim());
                                                                 return testData;
                                                }
                                                for (String regexUnit : regexUnits) {
                                                                matcher = Pattern.compile(regexUnit).matcher(testData.getReferenceChunk());
                                                                if(matcher.find()){
                                                                                    units=matcher.group();
                                                                                    testData.setUnits(units.trim());
                                                                                    return testData;
                                                                }
                                                }
                                }
                                return testData;
                            }
                            /////////////////////////
                            
                            //EXCEPTION - HEMO TEST DETECTION
                           private boolean isHemoTest(String line){
                               
                                        if(line.contains("LEUCOCYTES")){
                                            hemoFlag=true;
                                        }
                                        if(hemoFlag){
                                                        if(Pattern.compile("^X 10³").matcher(line).find()||
                                                                Pattern.compile("^X 10^6").matcher(line).find()||
                                                                    Pattern.compile("^fL").matcher(line).find()||
                                                                        Pattern.compile("^fL").matcher(line).find()||
                                                                            Pattern.compile("^%").matcher(line).find()||
                                                                                Pattern.compile("^pq").matcher(line).find()||
                                                                                    Pattern.compile("^g/dL").matcher(line).find()||
                                                                                        ((line.contains("Male")||line.contains("M ")||line.contains("MALE")||line.contains("M:"))&&(line.contains("Female")||line.contains("Female")||line.contains("F ")||line.contains("F:")))){
                                                                  
                                                                      return true;
                                                        }
                                                        
                                        }
                                       return false;
                                       
                           }
                           ////////////////////////
                           
                            //EXCEPTION - HEMO TEST DETERMINATION
                            private String[] hemoTestParameterDetermination(String line){
                                                String testName=null;
                                                String value=null;
                                                String range=null;
                                                String units=null;

                                                if(line.contains("PLATELETCRIT")){
                                                                 hemoFlag=false;
                                                  }

                                                //SETTING TEST NAMES//
                                                Matcher hemoTestNameMatch =Pattern.compile("[A-Z][A-Z][A-Z|-| |\\.||(||)||-|%]+").matcher(line);
                                                if(hemoTestNameMatch.find()){
                                                                testName=hemoTestNameMatch.group();
                                                                line=line.replace(testName, "");
                                                }
                                                if(line.contains("X 10³ / µL")){
                                                                line = line.replace("X 10³ / µL", "");
                                                                units="X 10³ / µL";
                                                }
                                                else if(line.contains("X 10^6/µL")){
                                                                line = line.replace("X 10^6/µL", "");
                                                                units="X 10^6/µL";
                                                }

                                                //SETTING VALUES//
                                                Matcher hemoValueMatch =Pattern.compile("([0-9]+)([.][0-9]+[ ])*").matcher(line);
                                                if(hemoValueMatch.find()){
                                                                value=hemoValueMatch.group();
                                                }
                                                if(value==null||value.equals("")){

                                                                value="Nil";
                                                }
                                                if(units!=null){
                                                                range=line+units;
                                                }
                                                else{
                                                                range=line;
                                                }
                                                if(range.contains("ABSOLUTE COUNT")){
                                                               range = range.replace("ABSOLUTE COUNT", "");
                                                               testName+=" ABSOLUTE COUNT";
                                                }
                                                else if(range.contains("SD(RDW-SD)")){
                                                                range = range.replace(" SD(RDW-SD)", "");
                                                               testName+=" SD(RDW-SD)";
                                                }
                                                
                                                return new String[]{testName,range,value};
                                           
                            }
                           ////////////////////////
                            
                            //FIND MEANING
                            public Report findMeaning(Report report){
                                  Double lessVal=null;
                                            Double moreVal=null;
                                            Double val=null;
                                            String[] rangeValues= new String[0];
                                           for(Profile profile: report.getProfileData()){
                                               for(Test test: profile.getTestData()){
                                try{
                                          
                                                   if(test.getRangeValues().size()>0){
                                                   switch(test.getRangeValues().get(0).getName()){
                                                       case "Range": 
                                              
                                                                            rangeValues=test.getRangeValues().get(0).getValue().split("-");
                                                                            lessVal=Double.parseDouble(rangeValues[0].trim());
                                                                            moreVal=Double.parseDouble(rangeValues[1].trim());
                                                                            val=Double.parseDouble(test.getValue().trim());
                                                      
                                                                            if(val>moreVal){
                                                                                test.setMeaning("High");
                                                                                profile.setWarning(true);
                                                                                test.setWarning(true);
                                                                            }
                                                                            else if(val<lessVal){
                                                                                test.setMeaning("Low");
                                                                                profile.setWarning(true);
                                                                                test.setWarning(true);
                                                                            
                                                                            }
                                                                            else{
                                                                                test.setMeaning("Normal");
                                                                            }
                                                                            
                                                                
                                                    
                                                           break;
                                                       case "LessRange": rangeValues=test.getRangeValues().get(0).getValue().split("<");
                                                                            lessVal=Double.parseDouble(rangeValues[1].trim());
                                                                            val=Double.parseDouble(test.getValue().trim());
                                                                            if(val<lessVal){
                                                                                test.setMeaning("Normal");
                                                                                
                                                                            }
                                                                            else{
                                                                                test.setMeaning("High");
                                                                                profile.setWarning(true);
                                                                                test.setWarning(true);
                                                                            }
                                                           break;
                                                       case "MoreRange": rangeValues=test.getRangeValues().get(0).getValue().split(">");
                                                                            moreVal=Double.parseDouble(rangeValues[1].trim());
                                                                            val=Double.parseDouble(test.getValue().trim());
                                                                            if(val>moreVal){
                                                                                test.setMeaning("Normal");
                                                                               
                                                                            }
                                                                            else{
                                                                                test.setMeaning("Low");
                                                                                 profile.setWarning(true);
                                                                                 test.setWarning(true);
                                                                            }
                                                           break;
                                                         case "Male": 
                                                           if(report.getGender().equalsIgnoreCase("M")){
                                                           if(test.getRangeValues().get(0).getValue().split("-").length==2){
                                                                                            rangeValues=test.getRangeValues().get(0).getValue().split("-");
                                                                                            lessVal=Double.parseDouble(rangeValues[0].trim());
                                                                                            moreVal=Double.parseDouble(rangeValues[1].trim());
                                                                                            val=Double.parseDouble(test.getValue().trim());
                                                                                            if(val>moreVal){
                                                                                                test.setMeaning("High");
                                                                                                profile.setWarning(true);
                                                                                                test.setWarning(true);
                                                                                            }
                                                                                            else if(val<lessVal){
                                                                                                test.setMeaning("Low");
                                                                                                profile.setWarning(true);
                                                                                                test.setWarning(true);
                                                                                            }
                                                                                            else{
                                                                                                test.setMeaning("Normal");
                                                                                            }
                                                                            }
                                                                            else if(test.getRangeValues().get(0).getValue().contains("<")){
                                                                                        rangeValues=test.getRangeValues().get(0).getValue().split("<");
                                                                                        lessVal=Double.parseDouble(rangeValues[1].trim());
                                                                                        val=Double.parseDouble(test.getValue().trim());
                                                                                        if(val<lessVal){
                                                                                            test.setMeaning("Normal");
                                                                                        }
                                                                                        else{
                                                                                            test.setMeaning("High");
                                                                                            profile.setWarning(true);
                                                                                            test.setWarning(true);
                                                                                        }
                                                                            }
                                                                            else if(test.getRangeValues().get(0).getValue().contains(">")){
                                                                                        rangeValues=test.getRangeValues().get(0).getValue().split(">");
                                                                                        moreVal=Double.parseDouble(rangeValues[1].trim());
                                                                                        val=Double.parseDouble(test.getValue().trim());
                                                                                        if(val>moreVal){
                                                                                            test.setMeaning("Normal");
                                                                                        }
                                                                                        else{
                                                                                            test.setMeaning("Low");
                                                                                            profile.setWarning(true);
                                                                                            test.setWarning(true);
                                                                                        }
                                                                            }
                                                           } 
                                                           else if(report.getGender().equalsIgnoreCase("F")){
                                                                                           if(test.getRangeValues().get(1).getValue().split("-").length==2){
                                                                                            rangeValues=test.getRangeValues().get(1).getValue().split("-");
                                                                                            lessVal=Double.parseDouble(rangeValues[0].trim());
                                                                                            moreVal=Double.parseDouble(rangeValues[1].trim());
                                                                                            val=Double.parseDouble(test.getValue().trim());
                                                                                            if(val>moreVal){
                                                                                                test.setMeaning("High");
                                                                                            profile.setWarning(true);
                                                                                            test.setWarning(true);
                                                                                            }
                                                                                            else if(val<lessVal){
                                                                                                test.setMeaning("Low");
                                                                                            profile.setWarning(true);
                                                                                            test.setWarning(true);
                                                                                            }
                                                                                            else{
                                                                                                test.setMeaning("Normal");
                                                                                            }
                                                                            }
                                                                            else if(test.getRangeValues().get(1).getValue().contains("<")){
                                                                                        rangeValues=test.getRangeValues().get(1).getValue().split("<");
                                                                                        lessVal=Double.parseDouble(rangeValues[1].trim());
                                                                                        val=Double.parseDouble(test.getValue().trim());
                                                                                        if(val<lessVal){
                                                                                            test.setMeaning("Normal");
                                                                                        }
                                                                                        else{
                                                                                            test.setMeaning("High");
                                                                                            profile.setWarning(true);
                                                                                            test.setWarning(true);
                                                                                        }
                                                                            }
                                                                            else if(test.getRangeValues().get(1).getValue().contains(">")){
                                                                                        rangeValues=test.getRangeValues().get(1).getValue().split(">");
                                                                                        moreVal=Double.parseDouble(rangeValues[1].trim());
                                                                                        val=Double.parseDouble(test.getValue().trim());
                                                                                        if(val>moreVal){
                                                                                            test.setMeaning("Normal");
                                                                                        }
                                                                                        else{
                                                                                            test.setMeaning("Low");
                                                                                            profile.setWarning(true);
                                                                                            test.setWarning(true);
                                                                                        }
                                                                            }
                                                           }
                                                           break;
                                                        case "Female": 
                                                           if(report.getGender().equalsIgnoreCase("F")){
                                                           if(test.getRangeValues().get(0).getValue().split("-").length==2){
                                                                                            rangeValues=test.getRangeValues().get(0).getValue().split("-");
                                                                                            lessVal=Double.parseDouble(rangeValues[0].trim());
                                                                                            moreVal=Double.parseDouble(rangeValues[1].trim());
                                                                                            val=Double.parseDouble(test.getValue().trim());
                                                                                            if(val>moreVal){
                                                                                                test.setMeaning("High");
                                                                                            profile.setWarning(true);
                                                                                            test.setWarning(true);
                                                                                            }
                                                                                            else if(val<lessVal){
                                                                                                test.setMeaning("Low");
                                                                                            profile.setWarning(true);
                                                                                            test.setWarning(true);
                                                                                            }
                                                                                            else{
                                                                                                test.setMeaning("Normal");
                                                                                            }
                                                                            }
                                                                            else if(test.getRangeValues().get(0).getValue().contains("<")){
                                                                                        rangeValues=test.getRangeValues().get(0).getValue().split("<");
                                                                                        lessVal=Double.parseDouble(rangeValues[1].trim());
                                                                                        val=Double.parseDouble(test.getValue().trim());
                                                                                        if(val<lessVal){
                                                                                            test.setMeaning("Normal");
                                                                                        }
                                                                                        else{
                                                                                            test.setMeaning("High");
                                                                                            profile.setWarning(true);
                                                                                            test.setWarning(true);
                                                                                        }
                                                                            }
                                                                            else if(test.getRangeValues().get(0).getValue().contains(">")){
                                                                                        rangeValues=test.getRangeValues().get(0).getValue().split(">");
                                                                                        moreVal=Double.parseDouble(rangeValues[1].trim());
                                                                                        val=Double.parseDouble(test.getValue().trim());
                                                                                        if(val>moreVal){
                                                                                            test.setMeaning("Normal");
                                                                                        }
                                                                                        else{
                                                                                            test.setMeaning("Low");
                                                                                            profile.setWarning(true);
                                                                                            test.setWarning(true);
                                                                                        }
                                                                            }
                                                           } 
                                                           else if(report.getGender().equalsIgnoreCase("M")){
                                                                                           if(test.getRangeValues().get(1).getValue().split("-").length==2){
                                                                                            rangeValues=test.getRangeValues().get(1).getValue().split("-");
                                                                                            lessVal=Double.parseDouble(rangeValues[1].trim());
                                                                                            moreVal=Double.parseDouble(rangeValues[1].trim());
                                                                                            val=Double.parseDouble(test.getValue().trim());
                                                                                            if(val>moreVal){
                                                                                                test.setMeaning("High");
                                                                                            profile.setWarning(true);
                                                                                            test.setWarning(true);
                                                                                            }
                                                                                            else if(val<lessVal){
                                                                                                test.setMeaning("Low");
                                                                                            profile.setWarning(true);
                                                                                            test.setWarning(true);
                                                                                            }
                                                                                            else{
                                                                                                test.setMeaning("Normal");
                                                                                            }
                                                                            }
                                                                            else if(test.getRangeValues().get(1).getValue().contains("<")){
                                                                                        rangeValues=test.getRangeValues().get(1).getValue().split("<");
                                                                                        lessVal=Double.parseDouble(rangeValues[1].trim());
                                                                                        val=Double.parseDouble(test.getValue().trim());
                                                                                        if(val<lessVal){
                                                                                            test.setMeaning("Normal");
                                                                                        }
                                                                                        else{
                                                                                            test.setMeaning("High");
                                                                                            profile.setWarning(true);
                                                                                            test.setWarning(true);
                                                                                        }
                                                                            }
                                                                            else if(test.getRangeValues().get(1).getValue().contains(">")){
                                                                                        rangeValues=test.getRangeValues().get(1).getValue().split(">");
                                                                                        moreVal=Double.parseDouble(rangeValues[1].trim());
                                                                                        val=Double.parseDouble(test.getValue().trim());
                                                                                        if(val>moreVal){
                                                                                            test.setMeaning("Normal");
                                                                                        }
                                                                                        else{
                                                                                            test.setMeaning("Low");
                                                                                            profile.setWarning(true);
                                                                                            test.setWarning(true);
                                                                                        }
                                                                            }
                                                           }
                                                           break;
                                                       case "NegEqPosRange":   
                                                                                            for(ReferenceRange ref:test.getRangeValues()){
                                                                                                if(ref.getName().equalsIgnoreCase("Eq")){
                                                                                                    rangeValues=ref.getValue().split("-");
                                                                                                    lessVal=Double.parseDouble(rangeValues[0].trim());
                                                                                                    moreVal=Double.parseDouble(rangeValues[1].trim());
                                                                                                    val=Double.parseDouble(test.getValue().trim());

                                                                                                    if(val>moreVal){
                                                                                                        test.setMeaning("Positive");
                                                                                                        profile.setWarning(true);
                                                                                                        test.setWarning(true);
                                                                                                    }
                                                                                                    else if(val<lessVal){
                                                                                                        test.setMeaning("Negative");
                                                                                                    }
                                                                                                    else{
                                                                                                        test.setMeaning("Equivocal");
                                                                                                        profile.setWarning(true);
                                                                                                        test.setWarning(true);
                                                                                                    }
                                                                                                }
                                                                                            }
                                                           
                                                                                            
                                                            break;
                                                       case "NilRange":     if(test.getValue().trim().equalsIgnoreCase("Nil")){
                                                                                                test.setMeaning("Normal");
                                                                                     }
                                                                                    else{
                                                                                                test.setMeaning("Abnormal");
                                                                                            profile.setWarning(true);
                                                                                            test.setWarning(true);
                                                                                    }
                                                        
                                                           break;
                                                       case "NegPosRange":    
                                                                                            for(ReferenceRange ref:test.getRangeValues()){
                                                                                            if(ref.getName().equalsIgnoreCase("Neg")){
                                                                                                        rangeValues=ref.getValue().split("<");
                                                                                                        lessVal=Double.parseDouble(rangeValues[1].trim());
                                                                                                        val=Double.parseDouble(test.getValue().trim());
                                                                                                        if(val<lessVal){
                                                                                                            test.setMeaning("Negative");
                                                                                                        }
                                                                                                        else{
                                                                                                            test.setMeaning("Positive");
                                                                                                            profile.setWarning(true);
                                                                                                            test.setWarning(true);
                                                                                                        }
                                                                                            }
                                                                                            }
                                                        
                                                           break; 
                                                       case "BorderlineRef":    
                                                                                            for(ReferenceRange ref:test.getRangeValues()){
                                                                                                if(ref.getName().equalsIgnoreCase("BorderRef")){
                                                                                                     rangeValues=ref.getValue().split("-");
                                                                                                    lessVal=Double.parseDouble(rangeValues[0].trim());
                                                                                                    moreVal=Double.parseDouble(rangeValues[1].trim());
                                                                                                    val=Double.parseDouble(test.getValue().trim());
                                                                                                    if(val>moreVal){
                                                                                                        test.setMeaning("Positive");
                                                                                                        profile.setWarning(true);
                                                                                                        test.setWarning(true);
                                                                                                    }
                                                                                                    else if(val<lessVal){
                                                                                                        test.setMeaning("Negative");
                                                                                                    }
                                                                                                    else{
                                                                                                        test.setMeaning("Border");
                                                                                                        profile.setWarning(true);
                                                                                                        test.setWarning(true);
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                           
                                                           break;
                                                       case "HeartRange":     
                                                                                            for(ReferenceRange ref:test.getRangeValues()){
                                                                                                if(ref.getName().equalsIgnoreCase("Avg")){
                                                                                                   
                                                                                                    rangeValues=ref.getValue().split("-");
                                                                                                      
                                                                                                        lessVal=Double.parseDouble(rangeValues[0].trim());
                                                                                                        moreVal=Double.parseDouble(rangeValues[1].trim());
                                                                                                        val=Double.parseDouble(test.getValue().trim());
                                                                                                        if(val>moreVal){
                                                                                                            test.setMeaning("High Risk");
                                                                                                            profile.setWarning(true);
                                                                                                            test.setWarning(true);
                                                                                                        }
                                                                                                        else if(val<lessVal){
                                                                                                            test.setMeaning("Low Risk");
                                                                                                        }
                                                                                                        else{
                                                                                                            test.setMeaning("Normal");
                                                                                                        }
                                                                                                }
                                                                                            }
                                                                                          
                                                           break;
                                                           case "NormalRef":     
                                                                                            for(ReferenceRange ref:test.getRangeValues()){
                                                                                                if(ref.getName().equalsIgnoreCase("BorderRef")){
                                                                                                    rangeValues=ref.getValue().split("-");
                                                                                                        lessVal=Double.parseDouble(rangeValues[0].trim());
                                                                                                        moreVal=Double.parseDouble(rangeValues[1].trim());
                                                                                                        val=Double.parseDouble(test.getValue().trim());
                                                                                                        if(val>moreVal){
                                                                                                            test.setMeaning("High");
                                                                                                            profile.setWarning(true);
                                                                                                            test.setWarning(true);
                                                                                                        }
                                                                                                        else if(val<lessVal){
                                                                                                            test.setMeaning("Normal");
                                                                                                        }
                                                                                                        else{
                                                                                                            test.setMeaning("Border");
                                                                                                            profile.setWarning(true);
                                                                                                            test.setWarning(true);
                                                                                                        }
                                                                                                }
                                                                                            }
                                                                                          
                                                           break;
                                                        case "PresenceRange":  
                                                             val=Double.parseDouble(test.getValue().trim());
                                                            for(ReferenceRange ref:test.getRangeValues()){
                                                                if(ref.getName().equalsIgnoreCase("Def")){
                                                                    rangeValues=ref.getValue().split("<");
                                                                    lessVal=Double.parseDouble(rangeValues[1].trim());
                                                                    if(val<lessVal){
                                                                        test.setMeaning("Deficient");
                                                                                            profile.setWarning(true);
                                                                                                            test.setWarning(true);
                                                                        break;
                                                                    }
                                                                }
                                                                else if(ref.getName().equalsIgnoreCase("Ins")){
                                                                    rangeValues=ref.getValue().split("-");
                                                                    lessVal=Double.parseDouble(rangeValues[0].trim());
                                                                    moreVal=Double.parseDouble(rangeValues[1].trim());
                                                                    if(val>=lessVal&&val<moreVal){
                                                                        test.setMeaning("Insufficient");
                                                                                            profile.setWarning(true);
                                                                                                            test.setWarning(true);
                                                                        break;
                                                                    }
                                                                }
                                                                else if(ref.getName().equalsIgnoreCase("Suf")){
                                                                    rangeValues=ref.getValue().split("-");
                                                                    lessVal=Double.parseDouble(rangeValues[0].trim());
                                                                    moreVal=Double.parseDouble(rangeValues[1].trim());
                                                                    if(val>=lessVal&&val<moreVal){
                                                                        test.setMeaning("Sufficient");
                                                                        break;
                                                                    }
                                                                }
                                                                else if(ref.getName().equalsIgnoreCase("Tox")){
                                                                    rangeValues=ref.getValue().split(">");
                                                                    moreVal=Double.parseDouble(rangeValues[1].trim());
                                                                    if(val>=moreVal){
                                                                        test.setMeaning("Toxic");
                                                                                            profile.setWarning(true);
                                                                                                            test.setWarning(true);
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                           break;
                                                         case "DiabeticRange":    
                                                            val=Double.parseDouble(test.getValue().trim());
                                                            for(ReferenceRange ref:test.getRangeValues()){
                                                                if(ref.getName().equalsIgnoreCase("Norm")){
                                                                    if(ref.getValue().contains("-")){
                                                                    rangeValues=ref.getValue().split("-");
                                                                    lessVal=Double.parseDouble(rangeValues[0].trim());
                                                                    moreVal=Double.parseDouble(rangeValues[1].trim());
                                                                    if(val>=lessVal&&val<moreVal){
                                                                        test.setMeaning("Normal");
                                                                        break;
                                                                    }
                                                                    }
                                                                    else if (ref.getValue().contains("<")){
                                                                         rangeValues=ref.getValue().split("<");
                                                                        lessVal=Double.parseDouble(rangeValues[1].trim());
                                                                        if(val<lessVal){
                                                                            test.setMeaning("Excellent");
                                                                            break;
                                                                        }
                                                                    }
                                                                }
                                                                else if(ref.getName().equalsIgnoreCase("Good")){
                                                                    rangeValues=ref.getValue().split("-");
                                                                    lessVal=Double.parseDouble(rangeValues[0].trim());
                                                                    moreVal=Double.parseDouble(rangeValues[1].trim());
                                                                    if(val>=lessVal&&val<moreVal){
                                                                        test.setMeaning("Good");
                                                                        break;
                                                                    }
                                                                }
                                                                else if(ref.getName().equalsIgnoreCase("Unsat")){
                                                                    rangeValues=ref.getValue().split("-");
                                                                    lessVal=Double.parseDouble(rangeValues[0].trim());
                                                                    moreVal=Double.parseDouble(rangeValues[1].trim());
                                                                    if(val>=lessVal&&val<moreVal){
                                                                        test.setMeaning("Unsatisfactory");
                                                                                            profile.setWarning(true);
                                                                                                            test.setWarning(true);
                                                                        break;
                                                                    }
                                                                }
                                                                else if(ref.getName().equalsIgnoreCase("Poor")){
                                                                    rangeValues=ref.getValue().split(">");
                                                                    moreVal=Double.parseDouble(rangeValues[1].trim());
                                                                    if(val>=moreVal){
                                                                        test.setMeaning("Poor");
                                                                                            profile.setWarning(true);
                                                                                                            test.setWarning(true);
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                             
                                                             
                                                           break;
                                                       
                                                        case "TimeRange":  
                                                            val=Double.parseDouble(test.getValue().trim());
                                                            for(ReferenceRange ref:test.getRangeValues()){
                                                                  if(ref.getValue().equalsIgnoreCase("07.00 - 10.00 A.M.")){
                                                                        if(report.getSampleCollectedDate().split(":")[0].split(" ")[3].equals("07")||report.getSampleCollectedDate().split(":")[0].split(" ")[3].equals("08")||report.getSampleCollectedDate().split(":")[0].split(" ")[3].equals("09")){
                                                                                            rangeValues=ref.getValue().split("-");
                                                                                            lessVal=Double.parseDouble(rangeValues[0].trim());
                                                                                            moreVal=Double.parseDouble(rangeValues[1].trim());
                                                                                            val=Double.parseDouble(test.getValue().trim());
                                                                                            if(val>moreVal){
                                                                                                test.setMeaning("High");
                                                                                            profile.setWarning(true);
                                                                                                            test.setWarning(true);
                                                                                            }
                                                                                            else if(val<lessVal){
                                                                                                test.setMeaning("Low");
                                                                                            }
                                                                                            else{
                                                                                                test.setMeaning("Normal");
                                                                                            }
                                                                        }
                                                                }
                                                               else if(ref.getValue().equalsIgnoreCase("04.00 - 08.00 P.M.")){
                                                                      if(report.getSampleCollectedDate().split(":")[0].split(" ")[3].equals("16")||report.getSampleCollectedDate().split(":")[0].split(" ")[3].equals("17")||report.getSampleCollectedDate().split(":")[0].split(" ")[3].equals("18")||report.getSampleCollectedDate().split(":")[0].split(" ")[3].equals("19")){
                                                                                            rangeValues=ref.getValue().split("-");
                                                                                            lessVal=Double.parseDouble(rangeValues[0].trim());
                                                                                            moreVal=Double.parseDouble(rangeValues[1].trim());
                                                                                            val=Double.parseDouble(test.getValue().trim());
                                                                                            if(val>moreVal){
                                                                                                test.setMeaning("High");
                                                                                            profile.setWarning(true);
                                                                                                            test.setWarning(true);
                                                                                            }
                                                                                            else if(val<lessVal){
                                                                                                test.setMeaning("Low");
                                                                                            }
                                                                                            else{
                                                                                                test.setMeaning("Normal");
                                                                                            }
                                                                        }
                                                                  }
                                                            }
                                                           break;
                                                         case "AgeRange":     
                                                                                                            test.setWarning(true);
                                                                                                test.setMeaning("MeaningNotFound");
                                                           break;
                                                         case "FemaleHormoneRange":  
                                                                                                            test.setWarning(true);
                                                                                                test.setMeaning("MeaningNotFound");   
                                                           break;
                                                         case "MaleHormoneRange":     
                                                                                                            test.setWarning(true);
                                                                                                test.setMeaning("MeaningNotFound");
                                                           break;
                                                         case "Exact":    
                                                                                                            test.setWarning(true);
                                                                                                test.setMeaning("MeaningNotFound");
                                                           break;
                                                         default:
                                                                                                            test.setWarning(true);
                                                                                                 test.setMeaning("MeaningNotFound");
                                                           break;
                                                   }
                                               }
                                            
                                }
                                catch(Exception e){
                                 e.printStackTrace();
                                                test.setWarning(true);
                                  test.setMeaning("MeaningNotFound");
                                }
                                   }
                                           }
                                     return report;      
                                
                                
                           }
                            //////////////////////
                  
                            
}
