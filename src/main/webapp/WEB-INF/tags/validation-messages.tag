<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" description="Pesan error form (sesuai bahasa aktif) untuk dibaca validate.js" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%-- {label}, {min}, dan {max} diisi validate.js. Pesan di file terjemahan tidak boleh mengandung tanda kutip ganda --%>
<script type="application/json" id="validation-messages">
{
  "required": "<fmt:message key="validation.required"/>",
  "choose": "<fmt:message key="validation.choose"/>",
  "email": "<fmt:message key="validation.email"/>",
  "pattern": "<fmt:message key="validation.pattern"/>",
  "min": "<fmt:message key="validation.min"/>",
  "max": "<fmt:message key="validation.max"/>",
  "step": "<fmt:message key="validation.step"/>",
  "number": "<fmt:message key="validation.number"/>",
  "invalid": "<fmt:message key="validation.invalid"/>"
}
</script>
