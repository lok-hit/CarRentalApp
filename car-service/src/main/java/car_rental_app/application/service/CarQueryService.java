package car_rental_app.application.service;

import car_rental_app.application.query.CarDto;
import car_rental_app.application.query.GetAvailableCarsQuery;
import car_rental_app.application.query.GetCarByIdQuery;
import car_rental_app.domain.model.Car;
import car_rental_app.domain.model.CarId;
import car_rental_app.domain.port.CarQueryPort;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class CarQueryService {

    private static final Logger log = Logger.getLogger(CarQueryService.class.getName());
    private final CarQueryPort carQueryPort;

    @Autowired
    public CarQueryService(CarQueryPort carQueryPort) {
        this.carQueryPort = carQueryPort;
    }


    @Transactional(readOnly = true)
    public Optional<Car> handle(GetCarByIdQuery query) {
        log.info(() -> "[" + trace() + "] Query: GetCarById id=" + query.carId());
        Optional<Car> result = carQueryPort.findById(new CarId(query.carId().value()));
        log.fine(() -> "[" + trace() + "] Query result for id=" + query.carId() + " found=" + result.isPresent());
        return result;
    }

    @Transactional(readOnly = true)
    public List<Car> handle(GetAvailableCarsQuery query) {
        log.info(() -> "[" + trace() + "] Query: GetAvailableCars");
        List<Car> result = carQueryPort.findAvailable();
        log.fine(() -> "[" + trace() + "] Query result availableCars=" + result.size());
        return result;
    }

    private String trace() {
        String traceId = MDC.get("traceId");
        String spanId = MDC.get("spanId");
        return "trace=" + (traceId != null ? traceId : "none") + " span=" + (spanId != null ? spanId : "none");
    }
}
