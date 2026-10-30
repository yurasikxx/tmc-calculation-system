package by.bsuir.tcs.service;

import by.bsuir.tcs.entity.Norm;
import by.bsuir.tcs.entity.Profession;
import by.bsuir.tcs.entity.TmcItem;
import by.bsuir.tcs.entity.TmcType;
import by.bsuir.tcs.repository.NormRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NormServiceTest {

    @Mock
    private NormRepository normRepository;
    @Mock
    private TmcItemService tmcItemService;
    @Mock
    private ProfessionService professionService;

    @InjectMocks
    private NormService normService;

    private TmcItem tmcItem;
    private Profession profession;

    @BeforeEach
    void setUp() {
        tmcItem = TmcItem.builder().id(10L).name("Перчатки")
                .type(TmcType.builder().id(1L).name("SIZ").build()).build();
        profession = Profession.builder().id(5L).name("Токарь").build();
    }

    @Test
    void create_success() {
        Norm norm = Norm.builder()
                .tmcItem(TmcItem.builder().id(10L).build())
                .profession(Profession.builder().id(5L).build())
                .quantity(2).periodMonths(6).build();

        when(tmcItemService.findById(10L)).thenReturn(tmcItem);
        when(professionService.findById(5L)).thenReturn(profession);
        when(tmcItemService.hasAccessToTmcItem(tmcItem)).thenReturn(true);
        when(normRepository.existsByTmcItemIdAndProfessionId(10L, 5L)).thenReturn(false);
        when(normRepository.saveAndFlush(any())).thenAnswer(i -> i.getArgument(0));

        Norm result = normService.create(norm);

        assertEquals(2, result.getQuantity());
        assertEquals(6, result.getPeriodMonths());
    }

    @Test
    void create_withNonPositiveQuantity_throwsException() {
        Norm norm = Norm.builder().quantity(0).periodMonths(6).build();
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> normService.create(norm));
        assertTrue(ex.getMessage().contains("Quantity must be positive"));
    }

    @Test
    void create_withNonPositivePeriod_throwsException() {
        Norm norm = Norm.builder().quantity(2).periodMonths(0).build();
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> normService.create(norm));
        assertTrue(ex.getMessage().contains("Period months must be positive"));
    }

    @Test
    void create_withDuplicate_throwsException() {
        Norm norm = Norm.builder()
                .tmcItem(TmcItem.builder().id(10L).build())
                .profession(Profession.builder().id(5L).build())
                .quantity(2).periodMonths(6).build();

        when(tmcItemService.findById(10L)).thenReturn(tmcItem);
        when(professionService.findById(5L)).thenReturn(profession);
        when(tmcItemService.hasAccessToTmcItem(tmcItem)).thenReturn(true);
        when(normRepository.existsByTmcItemIdAndProfessionId(10L, 5L)).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> normService.create(norm));
        assertTrue(ex.getMessage().contains("already exists"));
    }

    @Test
    void delete_withNoAccess_throwsException() {
        Norm norm = Norm.builder().id(1L).tmcItem(tmcItem).build();
        when(normRepository.findById(1L)).thenReturn(java.util.Optional.of(norm));
        when(tmcItemService.hasAccessToTmcItem(tmcItem)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> normService.delete(1L));
        assertTrue(ex.getMessage().contains("permission"));
    }
}