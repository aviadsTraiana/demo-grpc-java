package demo.example.server;

import demo.example.grpc.Coordinates;
import demo.example.grpc.Direction;
import demo.example.grpc.Route;
import demo.example.grpc.RouteServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.util.Random;

public class RouteServiceImpl extends RouteServiceGrpc.RouteServiceImplBase {
    @Override
    public void navigate(Coordinates request, StreamObserver<Route> responseObserver) {
        System.out.println(request);
        if(request.getCoordinatesCount()<2)
            throw new RuntimeException("There must be at least two coordinates");
        int srcX= request.getCoordinates(0).getLatitude();
        int srcY= request.getCoordinates(0).getLongitude();
        int destX=request.getCoordinates(1).getLatitude();
        int destY=request.getCoordinates(1).getLongitude();
        //some processing on data
        String description="Navigating from ("+srcX+","+srcY+") to ("+destX+","+destY+")";
        Route response = Route.newBuilder()
                .addDirection(getNextDirection())
                .setDescription(description)
                .build();
        //set the response on callback
        responseObserver.onNext(response);
        //since it is async call we need to call complete so the client will not hang waiting
        responseObserver.onCompleted();
    }

    private Direction getNextDirection() {
        return Direction.forNumber(new Random().nextInt(4));
    }
}
