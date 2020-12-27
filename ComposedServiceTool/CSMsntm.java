import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class CSMsntm {
	HashMap<Integer, HashMap<Integer, HashMap<Integer, ArrayList<Integer>>>> composedServices = new HashMap<Integer, HashMap<Integer, HashMap<Integer, ArrayList<Integer>>>>();
	HashMap<Integer, HashMap<Integer, Integer>> serviceReplicas = new HashMap<Integer, HashMap<Integer, Integer>>();
	HashMap<Integer, Integer> noOfServicesMap = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> noOfInputsMap = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> HAAvg = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> MSNTMAvg = new HashMap<Integer, Integer>();
	HashMap<Integer, Float> MSNTMAvgOut = new HashMap<Integer, Float>();
	HashMap<Integer, Float> HAAvg2 = new HashMap<Integer, Float>();
	HashMap<Integer, Float> MultiMSNTMAvg = new HashMap<Integer, Float>();
	HashMap<Integer, Float> SMSMTTMAvg = new HashMap<Integer, Float>();
	int noOfComposedServices = 0;
	float overallHAAvg = 0, overallMSNTMAvg = 0;

	public static void main(String[] args) {
		CSMsntm obj = new CSMsntm();
		obj.getInput();
		obj.compute1();
		obj.compute2();
		// Chapter 4
		obj.compute3();
		// Chapter 5
		obj.compute4();
		// Chapter 6
		obj.compute5();
		// Output of Result
		obj.outputResultToFile();
	}

	private void outputResultToFile() {
		try {
			FileWriter myWriter = new FileWriter("E:\\ComposedServiceTool\\tempResult.txt");
			myWriter.write(+noOfComposedServices+"\n");
			for(int i=1;i<=noOfComposedServices;i++) {
				myWriter.write(HAAvg2.get(i).toString()+"\n");
			}
			for(int i=1;i<=noOfComposedServices;i++) {
				myWriter.write(MSNTMAvgOut.get(i).toString()+"\n");
			}
			for(int i=1;i<=noOfComposedServices;i++) {
				myWriter.write(MultiMSNTMAvg.get(i).toString()+"\n");
			}
			for(int i=1;i<=noOfComposedServices;i++) {
				myWriter.write(SMSMTTMAvg.get(i).toString()+"\n");
			}
			myWriter.close();
			//System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	private void calOverallAvg() {
		float sumHA = 0, sumMSNTM = 0;
		for (int i = 1; i <= noOfComposedServices; i++) {
			sumHA += HAAvg.get(i);
			sumMSNTM += MSNTMAvg.get(i);
		}
		overallHAAvg = sumHA / noOfComposedServices;
		overallMSNTMAvg = sumMSNTM / noOfComposedServices;
	}

	private void getInput() {
		File f = new File("E:\\ComposedServiceTool\\Input.xls");
		try {
			Workbook wb = Workbook.getWorkbook(f);
			noOfComposedServices = wb.getNumberOfSheets();
			for (int i = 0; i < noOfComposedServices; i++) {
				int noOfServices = 0, noOfInputs = 0;
				Sheet sheet = wb.getSheet(i);
				noOfServices = sheet.getRows();
				noOfInputs = sheet.getColumns();// -1
				noOfServicesMap.put((i + 1), noOfServices);
				noOfInputsMap.put((i + 1), noOfInputs);
				HashMap<Integer, Integer> replicasList = new HashMap<Integer, Integer>();
				HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> services = new HashMap<Integer, HashMap<Integer, ArrayList<Integer>>>();
				for (int row = 0; row < noOfServices; row++) {
					HashMap<Integer, ArrayList<Integer>> servicesInputParam = new HashMap<Integer, ArrayList<Integer>>();
					for (int col = 0; col < noOfInputs; col++) {
						Cell c = sheet.getCell(col, row);
						if (col == 0) {
							replicasList.put((row + 1), Integer.parseInt(c.getContents()));
						} else {
							ArrayList<Integer> listService = new ArrayList<Integer>();
							String[] nextService = c.getContents().split(",");
							for (int strIter = 0; strIter < nextService.length; strIter++) {
								String s = nextService[strIter];
								if (s.equals("-")) {
									s = "0";
								}
								if (!listService.contains(Integer.parseInt(s))) {
									listService.add(Integer.parseInt(s));
								}
							}
							servicesInputParam.put(col, listService);
						}
					}
					services.put((row + 1), servicesInputParam);
				}
				serviceReplicas.put((i + 1), replicasList);
				composedServices.put((i + 1), services);
			}
		} catch (Exception e) {
			e.getMessage();
		}
	}

	private void print() {
		for (int iter = 1; iter <= noOfComposedServices; iter++) {
			for (int iter2 = 1; iter2 <= noOfServicesMap.get(iter); iter2++) {
				for (int iter3 = 1; iter3 <= (noOfInputsMap.get(iter) - 1); iter3++) {
					System.out.println(composedServices.get(iter).get(iter2).get(iter3));
				}
			}
		}
	}

	// For Hybrid Automata
	private void compute1() {
		for (int iter = 1; iter <= noOfComposedServices; iter++) {
			int sum = 0;
			// To print the Table
			/*
			 * System.out.
			 * println("|Service|nextService|No of NextService|No of stack operations|AckTime|ComputationTime|"
			 * );
			 */
			for (int iter2 = 1; iter2 <= noOfServicesMap.get(iter); iter2++) {
				int serviceCount = 0, operationsCount = 0, replicasCount = 0, maxReplica = 0;
				ArrayList<Integer> list = new ArrayList<Integer>();
				for (int iter3 = 1; iter3 <= (noOfInputsMap.get(iter) - 1); iter3++) {
					for (Integer i : composedServices.get(iter).get(iter2).get(iter3)) {
						if (i != 0 && !list.contains(i)) { // i = 0 is replica list, and contains is checked to remove
															// duplicate next services
							list.add(i);
							replicasCount = serviceReplicas.get(iter).get(i);
							if (replicasCount > maxReplica) {
								maxReplica = replicasCount;
							}
							operationsCount = operationsCount + replicasCount;
						}
					}
					serviceCount = serviceCount + composedServices.get(iter).get(iter2).get(iter3).size();
				}
				int ackTime = maxReplica == 0 ? 0 : (maxReplica + 1) * 5;
				sum = sum + ackTime + operationsCount;
				// To print the table
				/*
				 * System.out.println("| " + (iter2) + " | " + list + " | " + serviceCount +
				 * " | " + operationsCount + " | " + ackTime + " | " + (ackTime +
				 * operationsCount) + " | ");
				 */
			}
			HAAvg.put(iter, sum);
			// System.out.println(sum);
		}
	}

	// For MSNTM
	private void compute2() {
		for (int iter = 1; iter <= noOfComposedServices; iter++) {
			int sum = 0;
			// To print the table
			/*
			 * System.out.println("|Service|nextService|No of NextService|No of stack
			 * operations|AckTime|ComputationTime|");
			 */
			for (int iter2 = 1; iter2 <= noOfServicesMap.get(iter); iter2++) {
				int serviceCount = 0, operationsCount = 0, replicasCount = 0, maxReplica = 0;
				ArrayList<Integer> list = new ArrayList<Integer>();
				for (int iter3 = 1; iter3 <= (noOfInputsMap.get(iter) - 1); iter3++) {
					for (Integer i : composedServices.get(iter).get(iter2).get(iter3)) {
						if (i != 0 && !list.contains(i)) { // i = 0 is replica list, and contains is checked to remove
															// duplicate next services
							list.add(i);
							replicasCount = serviceReplicas.get(iter).get(i);
							if (replicasCount > maxReplica) {
								maxReplica = replicasCount;
							}
							operationsCount = maxReplica; // in this MSNTM operationsCount is the maxReplica count;
						}
					}
					serviceCount = serviceCount + composedServices.get(iter).get(iter2).get(iter3).size();
				}
				int ackTime = maxReplica == 0 ? 0 : 10; // 5_for_each_replica + 5_for_the_service = 10
				sum = sum + ackTime + operationsCount;
				// To print the table
				/*
				 * System.out.println("| " + (iter2) + " | " + list + " | " + serviceCount +
				 * " | " + operationsCount + " | " + ackTime + " | " + (ackTime +
				 * operationsCount) + " | ");
				 */
			}
			MSNTMAvg.put(iter, sum);
			// System.out.println(sum);
		}
	}
	// For MultiTape MSNTM
	private void compute4() {
		for (int i = 1; i <= noOfComposedServices; i++) {
			int avgValue = HAAvg.get(i);
			int[] array = {10,25,50,75,100};
			float sumHA = 0, sumofAvg = 0;
			for(int j = 0; j<5; j++) {
				float sumMSNTM = 0, avgMSNTM = 0;
				float value = avgValue * array[j];
				sumHA += value;
				for (int k = 2; k <= 10; k = k + 2) {
					sumMSNTM += (value / k);
				}
				avgMSNTM = sumMSNTM/5;
				//System.out.println(avgMSNTM);
				sumofAvg += avgMSNTM;				
			}
			HAAvg2.put(i, sumHA/5);
			MultiMSNTMAvg.put(i, sumofAvg/5);
		}

	}
	
	// For MutliStack and MultiTape MSNTM
	private void compute5() {
		for (int i = 1; i <= noOfComposedServices; i++) {
			int MSNTMAvgVal = MSNTMAvg.get(i);
			int[] array = {10,25,50,75,100};
			float  sumofAvg = 0;
			for(int j = 0; j<5; j++) {
				float sumMSNTM = 0, avgMSNTM = 0;
				float value = MSNTMAvgVal * array[j];
				for (int k = 2; k <= 10; k = k + 2) {
					sumMSNTM += (value / k);
				}
				avgMSNTM = sumMSNTM/5;
				//System.out.println(avgMSNTM);
				sumofAvg += avgMSNTM;				
			}
			SMSMTTMAvg.put(i, sumofAvg/5);
		}

	}
	
	// For MultiStack MSNTM
	private void compute3() {
		for (int i = 1; i <= noOfComposedServices; i++) {
			int MSNTMAvgVal = MSNTMAvg.get(i);
			int[] array = {10,25,50,75,100};
			float  sumofAvg = 0;
			for(int j = 0; j<5; j++) {
				float value = MSNTMAvgVal * array[j];
				sumofAvg += value;				
			}
			MSNTMAvgOut.put(i, sumofAvg/5);
		}

	}
}
