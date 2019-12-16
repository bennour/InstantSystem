package com.salah.instantsystem.mapper;

import com.salah.instantsystem.model.ParkingDetailsVM;
import com.salah.instantsystem.model.ParkingVM;
import com.salah.instantsystem.restclient.model.Fields;
import com.salah.instantsystem.restclient.model.Record;
import com.salah.instantsystem.restclient.model.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.salah.instantsystem.constant.Constant.OPEN_PARKING;
import static com.salah.instantsystem.constant.Constant.PARKING_URI;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ParkingMapperTest {

    @Autowired
    private ParkingMapper parkingMapper;
    @Autowired
    private RestTemplate restTemplate;

    private Response response;

    @Before
    public void setUp() {
        response = restTemplate.getForObject(PARKING_URI, Response.class);
    }

    @Test
    public void shouldMapToParkingDetailsVM() {
        Fields fields = response.getRecords().get(0).getFields();
        ParkingDetailsVM parkingDetailsVM = parkingMapper.toParkingDetailsVM(fields);

        assertParkingDetailsVM(parkingDetailsVM, fields);
    }

    @Test
    public void shouldMapToParkingListVM() {
        List<Record> records = response.getRecords();

        List<ParkingVM> parkingVMList = new ArrayList<>();
        for (Record record : records) {
            parkingVMList.add(parkingMapper.toParkingVM(record.getFields()));
        }

        assertThat(parkingVMList).hasSize(10);
        for (int i = 0; i < parkingVMList.size(); i++) {
            assertParkingVM(parkingVMList.get(i), records.get(i).getFields());
        }
    }

    // ----------------
    // PRIVATE METHODS
    // ----------------

    private void assertParkingDetailsVM(ParkingDetailsVM parkingDetailsVM, Fields fields) {
        assertParkingVM(parkingDetailsVM, fields);

        assertThat(fields.getOrgahoraires()).isEqualTo(fields.getOrgahoraires());
        assertThat(fields.getTarif15()).isEqualTo(fields.getTarif15());
        assertThat(fields.getTarif30()).isEqualTo(fields.getTarif30());
        assertThat(fields.getTarif1h()).isEqualTo(fields.getTarif1h());
        assertThat(fields.getTarif1h30()).isEqualTo(fields.getTarif1h30());
        assertThat(fields.getTarif2h()).isEqualTo(fields.getTarif2h());
        assertThat(fields.getTarif3h()).isEqualTo(fields.getTarif3h());
        assertThat(fields.getTarif4h()).isEqualTo(fields.getTarif4h());
    }

    private void assertParkingVM(ParkingVM parkingVM, Fields fields) {
        assertThat(parkingVM.getId()).isEqualTo(fields.getId());
        assertThat(parkingVM.getKey()).isEqualTo(fields.getKey());
        assertThat(parkingVM.getMax()).isEqualTo(fields.getMax());
        assertThat(parkingVM.getFree()).isEqualTo(fields.getFree());
        assertThat(parkingVM.isOpen()).isEqualTo(OPEN_PARKING.equalsIgnoreCase(fields.getStatus()));
        assertThat(parkingVM.getLatitude()).isEqualTo(fields.getGeo().get(0));
        assertThat(parkingVM.getLongitude()).isEqualTo(fields.getGeo().get(1));
    }
}
