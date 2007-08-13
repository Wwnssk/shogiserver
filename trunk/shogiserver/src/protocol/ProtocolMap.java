/*
 * ProtocolMap.java
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

package protocol;

import java.util.Hashtable;
import protocol.modules.ProtocolModule;
import protocol.ProtocolMapException;

/**
 *
 * @author Adrian Petrescu
 */
public class ProtocolMap {
    
    // We use a Hashtable to future-proof the Protocol map,
    // since it is synchronized.
    private Hashtable<String, ProtocolModule> map;
    
    /** Creates a new instance of ProtocolMap */
    public ProtocolMap() {
        map = new Hashtable<String, ProtocolModule>();
    }
    
    public void addProtocolModule(ProtocolModule pm) throws ProtocolMapException {
        // Check if a module that binds to the same first token is already registered with the server.
        if (map.containsKey(pm.getKey())){
            throw new ProtocolMapConflictException(pm, "Module with key " + pm.getKey() + " exists already.");
        }
        else {
            // We check to make sure all dependencies are met!
            String[] deps = pm.getDependencies();
            String[] tmpMissingDeps = new String[deps.length];
            int depCounter = 0;
            for (String dep : deps) {
                String [] sdep = dep.split(" ");
                if (sdep.length != 2 || !map.containsKey(sdep[0]) || !map.get(sdep[0]).getVersion().equals(sdep[1]) ) {
                    tmpMissingDeps[depCounter] = dep;
                    depCounter++;
                }
            }
            if (depCounter != 0) {
                String [] missingDeps = new String[depCounter];
                for (int i = 0; i < depCounter; i++) {
                    missingDeps[i] = tmpMissingDeps[i];
                }
                throw new ProtocolMapDependencyException(missingDeps,"The dependencies for module " +
                         pm.getName() + "were not met.");
            } else {
                // All dependencies have been met.
                map.put(pm.getKey(),pm);
            }
        }
    }
    
}
