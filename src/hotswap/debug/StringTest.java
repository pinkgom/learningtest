package hotswap.debug;

public class StringTest {

	public static void main(String[] args) {
		
		StringTest test = new StringTest();
		String content = "내용을 변경하면 바로 반영되는 문자열";
		String replacedContent = test.replaceString(content);
		
		test.printString("변경전 : " + content);
		test.printString("변경후 : " + replacedContent);
		
		
	}
	
	public String replaceString(String content) {
		String regExp = "\\s";
		
		return content.replaceAll(regExp, "-");
	}
	
	public void printString(String content) {
		System.out.println(content);
	}
}
