MBPosMatch {
    //match positions of all minibees against prerecorded positions
    classvar <>mbData;
    classvar <>resamplingFreq = 20;

    var <>threshold = 0.1;
    var <>recArray;
    var <>minibeeIDs;
    var <free = false;
    var <>speedlim = 1;
    var <curPosition, <task, <all;

    *new { arg threshold;
        ^super.newCopyArgs(threshold).init;
    }

    init {
        minibeeIDs = (10..14);
    }

    getPosition {
        var tmp;
        minibeeIDs.do{|id, idx|
            tmp[idx] = [
                mbData[id].x,
                mbData[id].y,
            ];
        };
        curPosition = tmp.flat.copy;
        resamplingFreq.reciprocal.wait;
    }

    compare {
        task = TaskProxy.new({
            inf.do({
                recArray.do({|i, idx|
                    if(i.notNil && free){
                        var diff;
                        diff = (i - curPosition).abs;
                        if(diff < threshold){
                            free = false;
                            SystemClock.sched(speedlim, {free = true});
                            ^idx;
                        }
                    }{
                        free = false
                        ^nil;
                    };
                });
                resamplingFreq.reciprocal.wait;
            });
        });
    }
}
