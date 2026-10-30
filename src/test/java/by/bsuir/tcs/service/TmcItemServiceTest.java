package by.bsuir.tcs.service;

import by.bsuir.tcs.entity.TmcItem;
import by.bsuir.tcs.entity.TmcType;
import by.bsuir.tcs.entity.Unit;
import by.bsuir.tcs.repository.TmcItemRepository;
import by.bsuir.tcs.repository.TmcTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TmcItemServiceTest {

    @Mock
    private TmcItemRepository tmcItemRepository;
    @Mock
    private TmcTypeRepository tmcTypeRepository;
    @Mock
    private UnitService unitService;

    @InjectMocks
    private TmcItemService tmcItemService;

    private TmcType sizType;
    private Unit unit;

    @BeforeEach
    void setUp() {
        sizType = TmcType.builder().id(1L).name("SIZ").build();
        unit = Unit.builder().id(1L).code("шт").name("Штука").build();
    }

    @Test
    void create_success() {
        TmcItem item = TmcItem.builder()
                .code("SIZ-001").name("Перчатки")
                .type(TmcType.builder().id(1L).build())
                .unit(Unit.builder().id(1L).build())
                .serviceLifeMonths(6).build();

        when(tmcItemRepository.findByCode("SIZ-001")).thenReturn(Optional.empty());
        when(tmcTypeRepository.findById(1L)).thenReturn(Optional.of(sizType));
        when(unitService.findById(1L)).thenReturn(unit);
        when(tmcItemRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        TmcItem result = tmcItemService.create(item, null);

        assertEquals("SIZ-001", result.getCode());
        assertEquals(sizType, result.getType());
    }

    @Test
    void create_withDuplicateCode_throwsException() {
        TmcItem item = TmcItem.builder().code("SIZ-001").build();
        when(tmcItemRepository.findByCode("SIZ-001"))
                .thenReturn(Optional.of(new TmcItem()));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> tmcItemService.create(item, null));
        assertTrue(ex.getMessage().contains("already exists"));
    }

    @Test
    void create_withEmptyCode_throwsException() {
        TmcItem item = TmcItem.builder().code("").build();
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> tmcItemService.create(item, null));
        assertTrue(ex.getMessage().contains("must not be empty"));
    }

    @Test
    void hasAccessToTmcItem_otRole_sizOnly() {
        TmcItem siz = TmcItem.builder().type(sizType).build();
        assertFalse(tmcItemService.hasAccessToTmcItem(siz));
    }

    @Test
    void findById_notFound_throwsException() {
        when(tmcItemRepository.findById(99L)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> tmcItemService.findById(99L));
        assertTrue(ex.getMessage().contains("not found"));
    }
}