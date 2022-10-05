package com.github.leeonky.dal.extensions;

import java.io.InputStream;
import java.util.stream.Stream;

public class SftpFileGroup extends FileGroup<SFtpFile> {
    private final SFtpFile sFtpFile;

    public SftpFileGroup(SFtpFile sFtpFile, String name) {
        super(name);
        this.sFtpFile = sFtpFile;
    }

    @Override
    protected InputStream open(SFtpFile subFile) {
        return ((SFtp.SubSFtpFile) subFile).download();
    }

    @Override
    protected SFtpFile createSubFile(String fileName) {
        return sFtpFile.access(fileName).orElseThrow(IllegalStateException::new);
    }

    @Override
    protected Stream<String> listFileName() {
        return sFtpFile.ls().stream().map(SFtpFile::name).filter(n -> n.startsWith(name + "."));
    }
}
