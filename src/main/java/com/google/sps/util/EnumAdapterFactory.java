// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.util;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * This class represents a factory for type adapters that serialize Enum classes into json strings
 * via the Gson library.
 *
 * @author guptamudit
 * @version 1.0
 */
public class EnumAdapterFactory implements TypeAdapterFactory {

  @Override
  public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
    Class<? super T> rawType = type.getRawType();
    if (rawType.isEnum()) {
      return new EnumTypeAdapter<T>();
    }
    return null;
  }

  public class EnumTypeAdapter<T> extends TypeAdapter<T> {
    @Override
    public void write(JsonWriter out, T value) throws IOException {
      if (value == null || !value.getClass().isEnum()) {
        out.nullValue();
        return;
      }

      try {
        out.beginObject();
        out.name("value");
        out.value(value.toString());
        Arrays.stream(Introspector.getBeanInfo(value.getClass()).getPropertyDescriptors())
            .filter(
                pd ->
                    pd.getReadMethod() != null
                        && !"class".equals(pd.getName())
                        && !"declaringClass".equals(pd.getName()))
            .forEach(
                pd -> {
                  try {
                    out.name(pd.getName());
                    out.value(String.valueOf(pd.getReadMethod().invoke(value)));
                  } catch (IllegalAccessException | InvocationTargetException | IOException e) {
                    e.printStackTrace();
                  }
                });
        out.endObject();
      } catch (IntrospectionException e) {
        e.printStackTrace();
      }
    }

    public T read(JsonReader in) throws IOException {
      // Properly deserialize the input (if you use deserialization)
      return null;
    }
  }
}
