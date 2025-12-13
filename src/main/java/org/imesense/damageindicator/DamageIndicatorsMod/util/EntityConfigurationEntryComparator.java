package org.imesense.damageindicator.DamageIndicatorsMod.util;

import org.imesense.damageindicator.DamageIndicatorsMod.core.EntityConfigurationEntry;
import org.imesense.damageindicator.DamageIndicatorsMod.core.Tools;
import java.util.Comparator;
import java.util.Map;
/* loaded from: input.jar:DamageIndicatorsMod/util/EntityConfigurationEntryComparator.class */
public class EntityConfigurationEntryComparator implements Comparator<EntityConfigurationEntry> {
    @Override // java.util.Comparator
    public int compare(EntityConfigurationEntry o1, EntityConfigurationEntry o2) {
        String str1;
        String str2;
        Map classToStringMapping = Tools.getEntityList();
        if (classToStringMapping.containsKey(o1.Clazz)) {
            str1 = (String) classToStringMapping.get(o1.Clazz);
        } else {
            str1 = o1.Clazz.getName();
        }
        if (classToStringMapping.containsKey(o2.Clazz)) {
            str2 = (String) classToStringMapping.get(o2.Clazz);
        } else {
            str2 = o2.Clazz.getName();
        }
        return str1.compareTo(str2);
    }
}
