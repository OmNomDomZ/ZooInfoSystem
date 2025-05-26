package v.rabetsky.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import v.rabetsky.dao.QueriesDAO;

import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/zoo/queries")
public class QueriesController {

    private final QueriesDAO queriesDAO;

    public QueriesController(QueriesDAO queriesDAO) {
        this.queriesDAO = queriesDAO;
    }

    @GetMapping
    public String getQueries() {
        return "zoo/queries/index";
    }

    @GetMapping("/{id}")
    public String getQuery(@PathVariable int id,
                           @RequestParam Map<String, String> p,
                           Model model) throws Exception {

        // 1) Получаем "сырые" DTO-записи
        List<?> rawRows;
        switch (id) {
            case 1:
                rawRows = queriesDAO.getEmployeesOverview();
                break;
            case 2:
                rawRows = queriesDAO.getEmployeesByAnimalType(p.get("type"));
                break;
            case 3:
                rawRows = queriesDAO.getEmployeesWithCageAccess(p.get("type"));
                break;
            case 4:
                rawRows = queriesDAO.getAnimalsInCage(Integer.parseInt(p.get("cageId")));
                break;
            case 5:
                rawRows = queriesDAO.getAnimalsNeedingHeat(p.get("type"));
                break;
            case 6:
                rawRows = queriesDAO.getAnimalsByVaccine(p.get("vaccine"));
                break;
            case 7:
                rawRows = queriesDAO.getCompatibleAnimals(p.get("type"));
                break;
            case 8:
                rawRows = queriesDAO.getSuppliersByPeriod(
                        LocalDate.parse(p.get("from")),
                        LocalDate.parse(p.get("to"))
                );
                break;
            case 9:
                rawRows = queriesDAO.getInternalFood();
                break;
            case 10:
                rawRows = queriesDAO.getAnimalsByFoodType(p.get("foodType"));
                break;
            case 11:
                rawRows = queriesDAO.getAnimalFullInfo(p.get("type"));
                break;
            case 12:
                rawRows = queriesDAO.getPotentialParents(
                        LocalDate.parse(p.get("start")
                ));
                break;
            case 13:
                rawRows = queriesDAO.getPartnerZoos(p.get("type"));
                break;
            default:
                throw new IllegalArgumentException("Unknown report id: " + id);
        }

        // 2) Конвертируем DTO → List<List<Object>>, вытягиваем заголовки
        List<List<Object>> rows = new ArrayList<>();
        List<String> headers = new ArrayList<>();

        if (!rawRows.isEmpty()) {
            Object first = rawRows.get(0);
            Class<?> rcClass = first.getClass();
            if (rcClass.isRecord()) {
                RecordComponent[] comps = rcClass.getRecordComponents();
                // заголовки = имена компонентов записи
                for (RecordComponent comp : comps) {
                    headers.add(comp.getName());
                }
                // строим строки
                for (Object rec : rawRows) {
                    List<Object> row = new ArrayList<>(comps.length);
                    for (RecordComponent comp : comps) {
                        Method accessor = comp.getAccessor();
                        Object value = accessor.invoke(rec);
                        row.add(value);
                    }
                    rows.add(row);
                }
            } else {
                // если не record, можно добавить другую логику
                throw new IllegalStateException("Expected record type, got " + rcClass);
            }
        }

        model.addAttribute("rows", rows);
        model.addAttribute("headers", headers);
        model.addAttribute("title", "Отчёт №" + id);
        return "zoo/queries/result";
    }
}
