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

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public abstract class MemberedTypeCode extends TypeCode
{
    protected MemberedTypeCode(int kind, String scope, String name)
    {
        super(kind);
        m_scope = scope;
        m_name = name;
        m_members = new LinkedHashMap<String, Member>();
    }

    public String getName()
    {
        return m_name;
    }

    public String getScopedname()
    {
        if(m_scope.isEmpty())
            return m_name;

        return m_scope + "::" + m_name;
    }

    public String getROS2Scopedname()
    {
        String newName = "";

        String[] names = m_name.split("_");
        if (names.length > 0) {
            for (String name : names) {
                if (name.length() >= 2 && Character.isUpperCase(name.charAt(0)) && Character.isLowerCase(name.charAt(1))) {
                    newName += name;
                }
                else {
                    newName += name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                }
            }
        }
        else {
            newName = m_name;
        }

        return newName;
/*
        if(m_scope == null || m_scope.isEmpty())
            return newName;

        String[] strs = m_scope.split("::");
        if (strs.length > 2) {
            return strs[0] + "::" + strs[1] + "::dds_::" + newName + "_";
        }

        return m_scope + "::dds_::" + newName + "_";
*/
    }

    public String getCScopedname()
    {
        if(m_scope.isEmpty())
            return m_name;

        String newName = "";
        String[] strs = m_scope.split("::");
        for (String str : strs) {
            newName += str.substring(0,1).toUpperCase() + str.substring(1);
        }

        return newName + "_" + m_name;
    }

    public String getJavaScopedname()
    {
        if(m_scope.isEmpty())
            return m_name;

        return m_scope.replace("::", ".") + "." + m_name;
    }

    public String getJniScopedname()
    {
        if(m_scope.isEmpty())
            return m_name;

        return m_scope.replace("::", "/") + "/" + m_name;
    }

    public String getScope()
    {
        return m_scope;
    }

    public String getCScope()
    {
        String newScope = "";
        String[] strs = m_scope.split("::");
        for (String str : strs) {
            newScope += str.substring(0,1).toUpperCase() + str.substring(1);
        }
        return newScope;
    }

    public boolean getHasScope()
    {
        return !m_scope.isEmpty();
    }

    public List<Member> getMembers()
    {
        return new ArrayList<Member>(m_members.values());
    }

    public boolean addMember(Member member)
    {
        if(!m_members.containsKey(member.getName()))
        {
            m_members.put(member.getName(), member);
            return true;
        }
        return false;
    }

    @Override
    public abstract String getCppTypename();

    @Override
    public abstract String getCTypename();

    @Override
    public abstract String getJavaTypename();

    @Override
    public abstract String getIdlTypename();

    @Override
    public boolean isIsPlain()
    {
        for (Member member : m_members.values())
        {
            if (!member.isIsPlain())
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isIsBounded()
    {
        for (Member member : m_members.values())
        {
            if (!member.isIsBounded())
            {
                return false;
            }
        }
        return true;
    }

    private String m_name = null;

    private String m_scope = null;

    private LinkedHashMap<String, Member> m_members = null;
}
