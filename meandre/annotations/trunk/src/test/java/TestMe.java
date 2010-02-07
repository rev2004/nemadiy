import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.imirsel.nema.annotations.parser.DataTypeAnnotationParser;
import org.imirsel.nema.annotations.parser.beans.DataTypeBean;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;


public class TestMe {
	

	public static void main(String[] args){
		DataTypeAnnotationParser dtp = new DataTypeAnnotationParser();
		HashMap<String,List<DataTypeBean>> hmap=dtp.getComponentDataType(DataTypeTestComponent.class);
	
		Iterator<String> itKey = hmap.keySet().iterator();
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.setMode(XStream.NO_REFERENCES);
   
		while(itKey.hasNext()){
			String key = itKey.next();
			
			System.out.println("Component Property: " + key);
			List<DataTypeBean> list = hmap.get(key);
			for(int i=0; i<list.size();i++ ){
				System.out.println("**"+list.get(i));
				
				
			}
			
		    String val = xstream.toXML(list);
	        System.out.println(val);		
	      System.out.println(val);
	      List<DataTypeBean> ltb=  (List<DataTypeBean>) xstream.fromXML(val);
	      System.out.println("LTB IS: "+ltb.toString());
		
	      
			
			
			
		}
		
			
		
		
	}

}
