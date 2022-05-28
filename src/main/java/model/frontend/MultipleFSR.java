
package model.frontend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultipleFSR {
        private List<FrontendSearchResponse> arrayFSP;
        

        public MultipleFSR(List<FrontendSearchResponse> arrayFSP){
            this.arrayFSP = arrayFSP;
        }

        public MultipleFSR(){

        }

        public void InitializeArray(){
            arrayFSP = new ArrayList<>();
        }

        public void setResponse(FrontendSearchResponse fsr) {
            this.arrayFSP.add(fsr);
        }

        public FrontendSearchResponse getResponse(int index) {
            return this.arrayFSP.get(index);
        }

        public int SizeFSP(){
            return this.arrayFSP.size();
        }

        public List<FrontendSearchResponse> ListaFSP(){
            return this.arrayFSP;
        }

}
