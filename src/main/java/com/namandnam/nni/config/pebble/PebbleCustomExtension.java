package com.namandnam.nni.config.pebble;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.namandnam.nni.account.service.UserSessionScopeBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mitchellbosecke.pebble.extension.AbstractExtension;
import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

@Component
public class PebbleCustomExtension extends AbstractExtension {

	@Autowired
	private UserSessionScopeBean sessionBean;
	
	@Override
    public Map<String, Function> getFunctions() {
        Map<String, Function> functions = new HashMap<String, Function>();
        functions.put("checkAuth", new CheckAuth());
        return functions;
    }

    /**
     * custom pebble 메소드를 만든다
     */
    class CheckAuth implements Function {

		@Override
		public List<String> getArgumentNames() {
			List<String> names = new ArrayList<String>();
            names.add("code");
            names.add("base");
            return names;
		}

		@Override
		public Object execute(Map<String, Object> args, PebbleTemplate self, EvaluationContext context, int lineNumber) {
			
			if( sessionBean == null || sessionBean.getAuth() == null )
				return false;
			
			if (args.get("code") == null || args.get("code").equals("") || args.get("base") == null || args.get("base").equals("")) {
                return null;
            }
			
			String code = String.valueOf(args.get("code"));
			int base = Integer.parseInt(args.get("base").toString());
			
			if( sessionBean.getAuth().get(code) >= base ) {
				return true;
			}else {
				return false;
			}
			
		}
        
    }
}
