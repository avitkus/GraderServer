package server.htmlBuilder.form.input;

import java.util.Arrays;


public class FileField extends Input implements IFileField {

    public FileField() {
            super();
            setType("file");
    }

    @Override
    public void setValue(String value) {
            addAttribute("value", value);
    }

    @Override
    public String getValue() {
            return getAttribute("value");
    }

    @Override
    public void addAcceptType(String mime, String... mimes) {
        String[] oldTypes = getAcceptTypes();
        StringBuilder type = new StringBuilder(mime);
        Arrays.stream(mimes).forEach((val) -> type.append(",").append(val));
        Arrays.stream(oldTypes).forEach((val) -> type.append(",").append(val));
        addAttribute("accept", type.toString());
    }

    @Override
    public String[] getAcceptTypes() {
        String types = getAttribute("accept");
        if (types == null) {
            return new String[]{};
        } else {
            return getAttribute("accept").split(",");
        }
    }

}
