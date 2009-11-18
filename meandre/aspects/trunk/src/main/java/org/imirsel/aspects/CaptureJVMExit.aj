package org.imirsel.aspects;
import org.meandre.core.ExecutableComponent;


public aspect CaptureJVMExit {
	pointcut captureExit(): call(* System.exit(..))
	&&
	cflow(withincode(* ExecutableComponent+.*(..)));
	void around(): captureExit(){
		System.out.println("Calling Exit is disabled from within the thirdparty libraries");
	}
}
