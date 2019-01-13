MBMovingAverage {

    classvar <>mbData;
    classvar <>resamplingFreq = 20;

    var <>minibeeID = 10;
    var <>windowSize = 20;
    var <task, <x, <y, <z, <delta, <all;

    *new { arg  minibeeID, windowSize;
        ^super.newCopyArgs(minibeeID, windowSize).init;
    }

    init {
    }

    movingSum { arg newValue=0.0;
        var oldValue, buffer, sum;
        oldValue = 0.0;
        sum = 0.0;
        buffer = 0.0 ! windowSize;
        oldValue = buffer.pop;
        buffer = buffer.addFirst(newValue);
        sum = sum + newValue - oldValue;
        sum;
    }

    calculateAverage {
        var in = 0.0 ! 4;
        var sum = 0.0 ! 4;
        var movingAverage = 0.0 ! 4;
        task = TaskProxy.new({
            inf.do{
                in = [
                    mbData[minibeeID].x,
                    mbData[minibeeID].y,
                    mbData[minibeeID].z,
                    mbData[minibeeID].delta
                ];
                in.do{|i, idx|
                    sum[idx] = this.movingSum(i).value;
                    movingAverage[idx] = sum[idx] / windowSize;
                };
                x = movingAverage[0];
                y = movingAverage[1];
                z = movingAverage[2];
                delta = movingAverage[3];
                all = movingAverage.copy;
                resamplingFreq.reciprocal.wait;
            }
        });
    }

    play {
        if(task.isNil){
            this.calculateAverage;
            task.play;
        }{
            "Task is playing. Stop it first".postln;
        };
    }

    stop {
        task.stop;
        task = nil;
    }
}
