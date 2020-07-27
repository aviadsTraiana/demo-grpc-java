import com.google.protobuf.InvalidProtocolBufferException;
import demo.example.grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Random;

public class ClientSide {
    public static void main(String[] args)  {
        ManagedChannel channel= ManagedChannelBuilder
                .forTarget("localhost:8080")
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext()
                .build();

        final String destination= "Tel-Aviv";
        System.out.println("getting coordinates of "+destination);
        GeoCodingGrpc.GeoCodingBlockingStub geoCodingStub = GeoCodingGrpc.newBlockingStub(channel);
        MapOfCoordinates mapOfCoordinates=
                geoCodingStub
                .getLocationCoordinate(
                        ListOfLocations
                                .newBuilder()
                                .addLocations(destination)
                                .build());
        Coordinate destCoordinate= mapOfCoordinates.getLocationsToCoordinatesOrThrow(destination);
        System.out.println(destination+" Coordinates: "+destCoordinate);
        //start navigating
        RouteServiceGrpc.RouteServiceBlockingStub routeStub = RouteServiceGrpc.newBlockingStub(channel);
        Route route = routeStub.navigate(
                Coordinates.newBuilder()
                .addCoordinates(getCoordinateFromGps())
                .addCoordinates(destCoordinate)
                        .build());
        System.out.println(route.getDescription());
        System.out.println("The directions are:");
        route.getDirectionList().forEach(System.out::println);

    }

    private static Coordinate getCoordinateFromGps()  {
        Random random = new Random();
        return Coordinate
                .newBuilder()
                .setLatitude(random.nextInt(1500))
                .setLongitude(random.nextInt(1500))
                .build();
    }
}
