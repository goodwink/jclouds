/**
 *
 * Copyright (C) 2011 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */
package org.jclouds.ibm.smartcloud.domain;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

/**
 * 
 * The current state of a location (datacenter)
 * 
 * @author Adrian Cole
 */
public class Location {
   public static Builder builder() {
      return new Builder();
   }

   public static class Builder {
      private String id;
      private String name;
      private String description;
      private String location;
      private State state;
      private Map<String, Map<String, String>> capabilities = Maps.newLinkedHashMap();

      public Builder id(String id) {
         this.id = id;
         return this;
      }

      public Builder name(String name) {
         this.name = name;
         return this;
      }

      public Builder description(String description) {
         this.description = description;
         return this;
      }

      public Builder location(String location) {
         this.location = location;
         return this;
      }

      public Builder state(State state) {
         this.state = state;
         return this;
      }

      public Builder capabilities(Map<String, Map<String, String>> capabilities) {
         this.capabilities = ImmutableMap.copyOf(capabilities);
         return this;
      }

      public Builder capability(String id) {
         return capability(id, ImmutableMap.<String, String> of());
      }

      public Builder capability(String id, Map<String, String> entries) {
         this.capabilities.put(id, entries);
         return this;
      }

      public Location build() {
         return new Location(id, name, description, location, state, capabilities);
      }
   }

   public static enum State {
      OFFLINE, ONLINE  ;
      @Override
      public String toString() {
         return this == OFFLINE ? "-1" : "1";
      }

      public static State fromValue(int state) {
         return state == -1 ? OFFLINE : ONLINE  ;
      }

   }

   private final String id;
   private final String name;
   private final String description;
   private final String location;
   private final State state;
   private final Map<String, Map<String, String>> capabilities;

   public Location(String id, String name, String description, String location, State state,
            Map<String, Map<String, String>> capabilities) {
      this.id = id;
      this.name = name;
      this.description = description;
      this.location = location;
      this.state = state;
      this.capabilities = ImmutableMap.copyOf(capabilities);
   }

   public String getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public State getState() {
      return state;
   }

   public String getDescription() {
      return description;
   }

   public String getLocation() {
      return location;
   }

   public Map<String, Map<String, String>> getCapabilities() {
      return capabilities;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((id == null) ? 0 : id.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      Location other = (Location) obj;
      if (id == null) {
         if (other.id != null)
            return false;
      } else if (!id.equals(other.id))
         return false;
      return true;
   }

   @Override
   public String toString() {
      return "[id=" + id + ", name=" + name + ", description=" + description + ", location=" + location + ", state="
               + state + ", capabilities=" + capabilities + "]";
   }

}
