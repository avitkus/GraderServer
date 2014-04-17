package server.httpTools.request;

import java.util.ArrayList;

public class MultipartRequestBodyFactory {
    private static final String boundaryRegex = "[a-zA-Z0-9/'/(/)/+/_/,-/./:/=/?]{1,70}";

    public static MultipartRequestBodyFactory getDefault() {
        return new MultipartRequestBodyFactory();
    }
    private final ArrayList<String> dataList;
    private final ArrayList<String> typeList;
    private final ArrayList<String> dispositionTypeList;
    private final ArrayList<String[]> dispositionList;
    
    protected MultipartRequestBodyFactory() {
        dataList = new ArrayList<>(2);
        typeList = new ArrayList<>(2);
        dispositionTypeList = new ArrayList<>(2);
        dispositionList = new ArrayList<>(2);
    }
    
    public void addPart(String data, String type, String dispositionType, String... disposition) {
        if (disposition.length % 2 != 0) {
            throw new IllegalArgumentException("Illegal disposition vales, key-value pairs required");
        }
        dataList.add(data);
        typeList.add(type);
        dispositionTypeList.add(dispositionType);
        dispositionList.add(disposition);
    }

    public IRequestBody buildBody(String boundaryVal) {
        if (boundaryVal.isEmpty()) {
            throw new IllegalArgumentException("Illegal boundary \"" + boundaryVal + ",\" boundary cannot be empty");
        } else if (boundaryVal.length() > 70) {
            throw new IllegalArgumentException("Illegal boundary \"" + boundaryVal + ",\" boundary cannot be greater than 70 characters");
        } else if (!boundaryVal.matches(boundaryRegex)) {
            throw new IllegalArgumentException("Illegal boundary \"" + boundaryVal + ",\" boundary contains illegal characters");
        }
        return new MultipartRequestBody() {
            @Override
            protected void init() {
                boundary = boundaryVal;
                MultipartContentFactory mfc = new MultipartContentFactory();
                for(int i = 0; i < dataList.size(); i ++) {
                    contents.add(mfc.buildContent(dataList.get(i), typeList.get(i), dispositionTypeList.get(i), dispositionList.get(i)));
                }
            }
        };
    }

    
    private static class MultipartContentFactory {
        public static MultipartContentFactory getDefault() {
            return new MultipartContentFactory();
        }
        
        protected MultipartContentFactory() {
        }
        
        MultipartContent buildContent(final String data, final String typeVal, final String dispositionTypeVal, final String... dispositions) {
            return new MultipartContent() {
                @Override
                protected void init() {
                    type = typeVal;
                    contents = data;
                    dispositionType = dispositionTypeVal;
                    for(int i = 0; i < dispositions.length; i += 2) {
                        disposition.put(dispositions[i], dispositions[i+1]);
                    }
                }
            };
        }
    }
}
