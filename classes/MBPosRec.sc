MBPosRec {
    //put all x and y values of all minibees into a 2-dimensional array
    classvar <>mbData;
    classvar <>minibeeIDs;
    classvar <>resamplingFreq = 20;

    var <>index, <recArray;

    *new { arg index;
        ^super.newCopyArgs(index).init;
    }

    init {
        minibeeIDs = (10..14);
        recArray = nil ! 128;
    }

    putPos {
        var tmp = nil ! minibeeIDs.size;
        var curPosition;
        minibeeIDs.do{|id, idx|
            tmp[idx] = [
                mbData[id].x,
                mbData[id].y,
            ];
        };
        recArray[index] = tmp.flat.copy;
    }
}
