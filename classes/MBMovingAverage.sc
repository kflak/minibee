MBMovingAverage {

    classvar <>mbData;
    classvar <>resamplingFreq = 20;

    var <>minibeeID = 10;
    var <>windowSize = 20;
    var <task, <x, <y, <z, <delta;

    *new { arg windowSize, minibeeID;
        ^super.newCopyArgs(windowSize, minibeeID).init;
    }

    init {
    }

    movingSum { arg in;
        var oldValue, newValue, buffer, sum;
        oldValue = 0.0;
        newValue = 0.0;
        sum = 0.0;
        buffer = 0.0 ! windowSize;

        newValue = in;
        oldValue = buffer.pop;
        buffer = buffer.addFirst(newValue);
        sum = sum + newValue - oldValue;
        sum;
    }

    createTask {
        var in;
        var sum = 0.0 ! 4;
        var movingAverage = 0.0 ! 4;
        var out = nil ! 4;
        task = TaskProxy.new({
            inf.do{
                in = [
                    mbData[minibeeID].x,
                    mbData[minibeeID].y,
                    mbData[minibeeID].z,
                    mbData[minibeeID].delta
                ];
                in.do{|i, idx|
                    sum[idx] = movingSum(i).value;
                    movingAverage[idx] = sum[idx] / windowSize;
                    resamplingFreq.reciprocal.wait;
                };
                x = movingAverage[0];
                y = movingAverage[1];
                z = movingAverage[2];
                delta = movingAverage[3];
            }
        })
    }

    play {
        if(task.isNil){
            this.createTask;
        }{
            "Task is playing. Stop it first".postln;
        };
    }

    stop {
        task.stop;
        task = nil;
    }
}
