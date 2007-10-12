/*
 * ConfigurationManager.java
 * Copyright (C) 2007  Adrian Petrescu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package service.configuration;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

/**
 *
 * @author Adrian Petrescu
 */
public class ConfigurationManager {
    
    private Map<String, String> configuration;
    
    /** Creates a new instance of ConfigurationManager */
    public ConfigurationManager() {
        configuration = Collections.synchronizedMap(new HashMap<String, String>());
        
        // Default values
        configuration.put("DATABASE_ENGINE", "MySQL");
        configuration.put("DATABASE_HOST", "localhost");
        configuration.put("DATABASE_PORT", "3306");
        configuration.put("DATABASE_USER", "shogi_admin");
        configuration.put("DATABASE_PASS", "shogi");
        configuration.put("DATABASE_NAME", "shogi_db");
        
        configuration.put("MAX_USERNAME_LENGTH", "20");
        configuration.put("MIN_USERNAME_LENGTH", "4");
        
        // Read the shogi.conf file for additional parameters
        // TODO: Implement shogi.conf parsing 
    }
    
    public boolean isConfigured(String key) {
        return configuration.containsKey(key);
    }
    
    public String getConfigurationOption(String key) {
        return configuration.get(key);
    }
    
    public String setConfigurationOption(String key, String value) {
        return configuration.put(key, value);
    }
}
