package demo.example.server;


import demo.example.grpc.Coordinate;
import demo.example.grpc.GeoCodingGrpc;
import demo.example.grpc.ListOfLocations;
import demo.example.grpc.MapOfCoordinates;
import io.grpc.stub.StreamObserver;

import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GeoCodingServiceImpl extends GeoCodingGrpc.GeoCodingImplBase {
    @Override
    public void getLocationCoordinate(ListOfLocations request, StreamObserver<MapOfCoordinates> responseObserver) {
        Map<String, Coordinate> map=request.getLocationsList()
                .stream().collect(Collectors.toMap(x-> x, this::calculateCoordinate));
        MapOfCoordinates mapOfCoordinates=MapOfCoordinates.newBuilder().putAllLocationsToCoordinates(map).build();
        responseObserver.onNext(mapOfCoordinates);
        responseObserver.onCompleted();
    }

    private Coordinate calculateCoordinate(String location){
        return Coordinate.newBuilder()
                .setLongitude(new Random().nextInt(500))
                .setLatitude(new Random().nextInt(500)).build();
    }

}
