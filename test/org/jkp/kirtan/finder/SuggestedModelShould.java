package org.jkp.kirtan.finder;

import java.util.Vector;

import javax.swing.ComboBoxModel;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SuggestedModelShould {

	Logger log = Logger.getLogger("Master");
	Vector<String> list = new Vector<String>(1700);
	ComboBoxModel model ;
	String inputs[],expectedOutputs[],expectedMultiOutputs[][];

	@Before
	public void setUp() {
		for(String title: CompleteMaster.OfficalTitle){
			list.add(title);
		}
	}
	
	public void check(String[] inputs,String expOutputs[]) {
		for(String input: inputs) {
			model = ComboKeyHandler.getTestableSuggestedModel(list,input);
			for(int i = 0; i<model.getSize(); i++){
				assertEquals("["+input+"] -> ",expOutputs[i],model.getElementAt(i));
			}
		}
	}

	public void check(String[] inputs,String expOutputs[][]) {
		for(int i =0;i< inputs.length; i++) {
			model = ComboKeyHandler.getTestableSuggestedModel(list,inputs[i]);
			for(int o = 0; o<model.getSize(); o++){
				assertEquals("["+inputs[i]+"] -> ",expOutputs[i][o],model.getElementAt(o));
			}
		}
	}

	public void giveRightSingleResults() {
		inputs = new String[]{"so saras","saras so","sarasuratiras"};
		expectedOutputs = new String[]{
				"sarasuratiras saras so rati rasa_pr2_ras_860_22",
				"sarasuratiras saras so rati rasa_pr2_ras_860_22",
				"sarasuratiras saras so rati rasa_pr2_ras_860_22"
		};
		check(inputs,expectedOutputs);
	}
	

	@Test
	public void giveRightMultipleResult() {
		inputs = new String[]{"\"shri radhe radhe radhe"};
		expectedMultiOutputs = new String[][]{
				{
				"radhe radhe shri radhe radhe radhe_ym1_132_40",
				"shri radhe radhe radhe radhe radhe radhe_br1_145_84",
				"shri radhe radhe radhe radhe radhe_br1_148_86"}
		};
		
		check(inputs,expectedMultiOutputs);
	}
}
