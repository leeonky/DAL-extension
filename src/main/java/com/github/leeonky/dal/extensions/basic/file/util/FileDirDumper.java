package com.github.leeonky.dal.extensions.basic.file.util;

import com.github.leeonky.dal.runtime.Data;
import com.github.leeonky.dal.runtime.inspector.Dumper;
import com.github.leeonky.dal.runtime.inspector.DumpingContext;

import java.io.File;

public class FileDirDumper implements Dumper {

    @Override
    public void dumpDetail(Data data, DumpingContext context) {
        DumpingContext sub = context.append("java.io.File").appendThen(" ")
                .append(((File) data.getInstance()).getPath() + "/").sub();
        data.getDataList().forEach(sub.newLine()::dump);
    }

    @Override
    public void dump(Data data, DumpingContext context) {
        DumpingContext sub = context.append(((File) data.getInstance()).getName() + "/").indent();
        data.getDataList().forEach(sub.newLine()::dump);
    }
}
