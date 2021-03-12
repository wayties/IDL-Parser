// Copyright 2016 Proyectos y Sistemas de Mantenimiento SL (eProsima).
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.eprosima.idl.parser.typecode;

import com.eprosima.idl.parser.tree.Annotation;
import com.eprosima.idl.context.Context;

import org.antlr.stringtemplate.StringTemplate;


public class EnumTypeCode extends MemberedTypeCode
{
    public EnumTypeCode(String scope, String name)
    {
        super(Kind.KIND_ENUM, scope, name);
    }

    @Override
    public boolean isPrimitive() {return true;}

    @Override
    public boolean isIsType_c(){return true;}

    @Override
    public String getTypeIdentifier()
    {
        return "EK_MINIMAL";
    }

    @Override
    public boolean isObjectType() { return true; }

    public void addMember(EnumMember member)
    {
        addMember((Member)member);
    }

    @Override
    public String getCppTypename()
    {
        StringTemplate st = getCppTypenameFromStringTemplate();
        st.setAttribute("name", getScopedname());
        return st.toString();
    }

    @Override
    public String getCTypename()
    {
        StringTemplate st = getCTypenameFromStringTemplate();
        st.setAttribute("name", getCScopedname());
        return st.toString();
    }

    @Override
    public String getJavaTypename()
    {
        StringTemplate st = getJavaTypenameFromStringTemplate();
        st.setAttribute("name", getJavaScopedname());
        return st.toString();
    }

    @Override
    public String getIdlTypename()
    {
        StringTemplate st = getIdlTypenameFromStringTemplate();
        st.setAttribute("name", getScopedname());
        return st.toString();
    }

    @Override
    public String getInitialValue()
    {
        if(getMembers().size() > 0)
        {
            return (getScope() != null ? getScope() + "::" : "") + getMembers().get(0).getName();
        }

        return "";
    }

    @Override
    public String getJavaInitialValue()
    {
        if(getMembers().size() > 0)
        {
            return javapackage + getJavaScopedname() + "." + getMembers().get(0).getName();
        }

        return "";
    }

    /*public Pair<Integer, Integer> getMaxSerializedSize(int currentSize, int lastDataAligned)
    {
        int size = getSize();

        if(size <= lastDataAligned)
        {
            return new Pair<Integer, Integer>(currentSize + size, size);
        }
        else
        {
            int align = (size - (currentSize % size)) & (size - 1);
            return new Pair<Integer, Integer>(currentSize + size + align, size);
        }
    }

    public int getMaxSerializedSizeWithoutAlignment(int currentSize)
    {
        return currentSize + getSize();
    }*/

    @Override
    public String getSize()
    {
        if (m_enum_bound <= 8)
        {
            return "1";
        }
        else if (m_enum_bound <= 16)
        {
            return "2";
        }
        else if (m_enum_bound <= 32)
        {
            return "4";
        }
        else if (m_enum_bound <= 64)
        {
            return "8";
        }
        return "4";
    }

    public int getEnumBound()
    {
        return m_enum_bound;
    }

    public String getBoundType()
    {
        if (m_enum_bound <= 8)
        {
            return " : uint8_t";
        }
        else if (m_enum_bound <= 16)
        {
            return " : uint16_t";
        }
        else if (m_enum_bound <= 32)
        {
            return " : uint32_t";
        }
        else if (m_enum_bound <= 64)
        {
            return " : uint64_t";
        }
        return "";
    }

    public String getCastingType()
    {
        if (m_enum_bound <= 8)
        {
            return "uint8_t";
        }
        else if (m_enum_bound <= 16)
        {
            return "uint16_t";
        }
        else if (m_enum_bound <= 32)
        {
            return "uint32_t";
        }
        else if (m_enum_bound <= 64)
        {
            return "uint64_t";
        }
        return "";
    }

    @Override
    public void addAnnotation(Context ctx, Annotation annotation)
    {
        super.addAnnotation(ctx, annotation);
        if (annotation.getName().equals("enum_bound"))
        {
            m_enum_bound = Integer.parseInt(annotation.getValue());
        }
    }

    private Integer m_enum_bound = 32;
}
